package com.teknophase.chat.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.teknophase.chat.data.AppDatabase
import com.teknophase.chat.data.model.ChatListModel
import com.teknophase.chat.data.model.Message
import com.teknophase.chat.data.model.MessageDeliveredModel
import com.teknophase.chat.data.model.MessageDestinationType
import com.teknophase.chat.data.model.MessageReadModel
import com.teknophase.chat.data.model.MessageStatus
import com.teknophase.chat.data.model.MessageType
import com.teknophase.chat.data.request.MessageRequest
import com.teknophase.chat.data.response.MessageAcknowledgeResponse
import com.teknophase.chat.data.response.UserResponse
import com.teknophase.chat.data.state.ChatState
import com.teknophase.chat.network.SocketManager
import com.teknophase.chat.network.repositories.AuthRepository
import com.teknophase.chat.network.repositories.MessageRepository
import com.teknophase.chat.network.repositories.SOCKET_CHAT_ACKNOWLEDGE
import com.teknophase.chat.network.repositories.SOCKET_CHAT_DELIVERED
import com.teknophase.chat.network.repositories.SOCKET_CHAT_MESSAGE
import com.teknophase.chat.network.repositories.SOCKET_CHAT_READ
import com.teknophase.chat.providers.AuthState
import com.teknophase.chat.util.getFormattedTimeForMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository,
    private val gson: Gson
) :
    ViewModel() {
    private var _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState
    private val apiCooldownMillis = 1000L  // Cooldown time in milliseconds
    private val messageTimeoutMillis = 15000L  // Message Timeout in milliseconds
    private val handler = Looper.myLooper()?.let { Handler(it) }
    private var runnable: Runnable? = null

    init {
        GlobalScope.launch(Dispatchers.IO) {
            val chatList =
                AppDatabase.db?.chatListRepository()?.getAll()?.associateBy { it.username }
            chatList?.let {
                _chatState.value = _chatState.value.copy(userList = it)
            }
            val messages = AppDatabase.db?.messageRepository()?.getAll()
            messages?.let {
                _chatState.value = _chatState.value.copy(messages = it)
            }
            messageRepository.addOnChatListener(chatListener)
            messageRepository.addOnAcknowledgeListener(acknowledgeListener)
            messageRepository.addOnDeliveredListener(deliveredListener)
            messageRepository.addOnReadListener(readListener)

            try {
                val socket = SocketManager.getSocket()
                socket.on(Socket.EVENT_CONNECT) {
                    messages?.filter {
                        it.messageStatus == MessageStatus.QUEUED
                    }?.forEach {
                        GlobalScope.launch(Dispatchers.IO) {
                            val messageRequest = MessageRequest(
                                requestId = it.messageId,
                                sender = it.source,
                                receiver = it.destination,
                                content = it.content,
                                sentAt = it.sentAt
                            )
                            messageRepository.sendMessage(messageRequest)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SocketError", "Error initialising onConnect event")
            }
        }
    }


    private val chatListener = Emitter.Listener { _message ->
        Log.d(SOCKET_CHAT_MESSAGE, _message[0].toString())

        GlobalScope.launch(Dispatchers.IO) {
            try {
                var message: Message = gson
                    .fromJson(_message[0].toString(), Message::class.java)
                val userList = _chatState.value.userList.toMutableMap()
                message = message.copy(receivedAt = Date())

                if (!userList.containsKey(message.source)) {
                    var chatListModel = createChatListModel(message)
                    try {
                        val user = authRepository.getUserProfile(message.source)
                        chatListModel = createChatListModel(message, user)
                    } catch (e: Exception) {
                        Log.e(
                            "RetrofitError",
                            "Unable to fetch user profile. " + e.message.toString()
                        )
                    }
                    try {
                        userList[message.source] = chatListModel
                        AppDatabase.db?.chatListRepository()?.save(chatListModel)
                    } catch (e: Exception) {
                        Log.e("RoomError", "Unable to store user profile. " + e.message.toString())
                    }

                } else {
                    var count = userList[message.source]!!.unread
                    val isSent = message.source == AuthState.getUser()?.username
                    if (!isSent && count != null) count += 1
                    if (!isSent && count == null) count = 1
                    val chatListModel = userList[message.source]!!.copy(
                        unread = count,
                        message = message.content,
                        time = getFormattedTimeForMessage(message.receivedAt!!)
                    )
                    userList[message.source] = chatListModel
                    AppDatabase.db?.chatListRepository()?.update(chatListModel)
                }

                val updatedMessages = _chatState.value.messages.plusElement(message)
                _chatState.value = _chatState.value.copy(
                    messages = updatedMessages,
                    userList = userList
                )

                val deliveredModel = MessageDeliveredModel(
                    messageId = message.messageId,
                    source = message.source,
                    acknowledgedBy = AuthState.getUser()!!.username
                )
                AppDatabase.db?.messageRepository()
                    ?.save(
                        message.copy(
                            delivered = true,
                            messageStatus = MessageStatus.DELIVERED
                        )
                    )
                messageRepository.onDelivered(deliveredModel)
                if (chatState.value.fetchedUser != null
                    && chatState.value.fetchedUser?.username == message.source
                ) {
                    val readModel = MessageReadModel(
                        messageId = message.messageId,
                        source = message.source,
                        acknowledgedBy = AuthState.getUser()!!.username
                    )
                    messageRepository.onRead(readModel)
                }

            } catch (e: Exception) {
                Log.e("MessageParseError", "Unable to parse received message" + e.message)
                e.printStackTrace()
            }
        }
    }

    private val acknowledgeListener = Emitter.Listener { _acknowledge ->
        Log.d(SOCKET_CHAT_ACKNOWLEDGE, _acknowledge[0].toString())
        try {
            val acknowledgeResponse =
                Gson().fromJson(_acknowledge[0].toString(), MessageAcknowledgeResponse::class.java)
            var messages = _chatState.value.messages

            messages = messages.map {
                if (it.messageId == acknowledgeResponse.requestId) {
                    val updatedMessage = it.copy(
                        messageId = acknowledgeResponse.messageId,
                        messageStatus = MessageStatus.SENT
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            AppDatabase.db?.messageRepository()?.delete(it)
                            AppDatabase.db?.messageRepository()?.save(updatedMessage)
                        } catch (e: Exception) {
                            Log.e("RoomError", e.message.toString())
                        }
                    }
                    updatedMessage
                } else it
            }

            _chatState.value =
                _chatState.value.copy(
                    messages = messages
                )
        } catch (e: Exception) {
            Log.e("AcknowledgeParseError", "Unable to parse Acknowledge message")
        }
    }

    private val deliveredListener = Emitter.Listener { _deliveredModel ->
        Log.d(SOCKET_CHAT_DELIVERED, _deliveredModel[0].toString())
        try {
            val deliveredModel =
                Gson().fromJson(_deliveredModel[0].toString(), MessageDeliveredModel::class.java)
            var messages = _chatState.value.messages

            messages = messages.map {
                if (it.messageId == deliveredModel.messageId) {
                    val updatedMessage = it.copy(
                        messageStatus = MessageStatus.DELIVERED,
                        delivered = true,
                        receivedAt = deliveredModel.deliveredAt
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            AppDatabase.db?.messageRepository()?.update(updatedMessage)
                        } catch (e: Exception) {
                            Log.e("RoomError", e.message.toString())
                        }
                    }
                    updatedMessage
                } else it
            }

            _chatState.value =
                _chatState.value.copy(
                    messages = messages
                )
        } catch (e: Exception) {
            Log.e("DeliveredParseError", "Unable to parse Delivered message")
        }
    }

    private val readListener = Emitter.Listener { _readModel ->
        Log.d(SOCKET_CHAT_READ, _readModel[0].toString())
        try {
            val readModel =
                Gson().fromJson(_readModel[0].toString(), MessageReadModel::class.java)
            var messages = _chatState.value.messages

            messages = messages.map {
                if (it.messageId == readModel.messageId && it.destination != AuthState.getUser()!!.username) {
                    val updatedMessage = it.copy(
                        messageStatus = MessageStatus.READ,
                        read = true,
                        readAt = readModel.readAt
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            AppDatabase.db?.messageRepository()?.update(updatedMessage)
                        } catch (e: Exception) {
                            Log.e("RoomError", e.message.toString())
                        }
                    }
                    updatedMessage
                } else it
            }

            _chatState.value =
                _chatState.value.copy(
                    messages = messages
                )
        } catch (e: Exception) {
            Log.e("ReadParseError", "Unable to parse Read message")
        }
    }

    fun onMessageChange(text: String) {
        _chatState.value = _chatState.value.copy(clipBoard = text)
    }

    fun onSend(username: String) {
        if (chatState.value.clipBoard.isEmpty()) return
        val messageRequest = MessageRequest(
            sender = AuthState.getUser()?.username.toString(),
            receiver = username,
            content = chatState.value.clipBoard
        )
        val message = Message(
            messageId = messageRequest.requestId,
            source = messageRequest.sender,
            destination = messageRequest.receiver,
            content = messageRequest.content,
            destinationType = MessageDestinationType.values()[messageRequest.destinationType], // Todo: Make this future proof by adding default
            sentAt = messageRequest.sentAt,
            messageType = MessageType.values()[messageRequest.messageType],
            hasAttachment = messageRequest.hasAttachment
        )

        GlobalScope.launch(Dispatchers.IO) {
            if (SocketManager.isConnected.value) {
                messageRepository.sendMessage(messageRequest)
                Log.d("Sent", messageRequest.toString())
            } else Log.e("Socket", "Socket not connected")


            val userList = _chatState.value.userList.toMutableMap()
            if (!userList.containsKey(message.destination)) {
                val chatListModel = createChatListModel(message, chatState.value.fetchedUser)
                try {
                    userList[message.destination] = chatListModel
                    AppDatabase.db?.chatListRepository()?.save(chatListModel)
                } catch (e: Exception) {
                    Log.e("RoomError", "Unable to store user profile. " + e.message.toString())
                }
            } else {
                userList[message.destination] = userList[message.destination]!!.copy(
                    message = message.content,
                    time = getFormattedTimeForMessage(message.sentAt)
                )
                try {
                    AppDatabase.db?.chatListRepository()?.update(userList[message.destination]!!)
                } catch (e: Exception) {
                    Log.e("RoomError", "Unable to store user profile. " + e.message.toString())
                }
            }
            val updatedMessages = chatState.value.messages.plusElement(
                message
            )
            _chatState.value = _chatState.value.copy(
                messages = updatedMessages,
                clipBoard = ""
            )
            _chatState.value = _chatState.value.copy(userList = userList)

            try {
                AppDatabase.db?.messageRepository()?.save(message)
            } catch (e: Exception) {
                Log.e("RoomError", e.message.toString())
            }

            handler?.postDelayed({
                GlobalScope.launch(Dispatchers.IO) {
                    if (!chatState.value.messages.none { it.messageId == messageRequest.requestId }) {
                        val updatedMessage = message.copy(messageStatus = MessageStatus.FAILED)
                        val messages = chatState.value.messages.map {
                            if (it.messageId == messageRequest.requestId) updatedMessage
                            else it
                        }
                        _chatState.value = _chatState.value.copy(messages = messages)
                        AppDatabase.db?.messageRepository()?.update(updatedMessage)
                        Log.d("MessageFail Updated", messageRequest.requestId)
                    }
                }

            }, messageTimeoutMillis)
        }
    }

    fun markRead(username: String) {
        val userList = _chatState.value.userList.toMutableMap()
        userList[username] = userList[username]!!.copy(unread = null)
        _chatState.value = _chatState.value.copy(
            userList = userList
        )

        GlobalScope.launch(Dispatchers.IO) {
            val userMessages = chatState.value.messages.filter {
                it.source == username &&
                        it.read != true
            }
            Log.d("unreadMessagesCount", userMessages.count().toString())
            for (message in userMessages) {
                try {
                    messageRepository.onRead(
                        MessageReadModel(
                            messageId = message.messageId,
                            source = message.source,
                            acknowledgedBy = AuthState.getUser()!!.username
                        )
                    )
                } catch (e: Exception) {
                    Log.e("SocketError", e.message.toString())
                }
            }

        }
    }

    private fun createChatListModel(message: Message, user: UserResponse? = null): ChatListModel {
        val isSent = message.source == AuthState.getUser()!!.username
        val username =
            if (isSent) message.destination else message.source
        return ChatListModel(
            username = username,
            name = if (user?.displayName != null) user.displayName else username,
            message = message.content,
            time = getFormattedTimeForMessage(if (isSent) message.sentAt else message.receivedAt!!),
            profileUrl = if (user?.profileUrl != null) user.profileUrl else "",
            unread = if (isSent) null else 1,
            userId = UUID.randomUUID().toString()
        )
    }

    private fun checkUsernameAvailability(username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            _chatState.value = _chatState.value.copy(checkingUsername = true)
            if (username.isNotEmpty()) {
                try {
                    val available = authRepository.checkUsernameAvailability(username)
                    if (!available) {
                        val user = authRepository.getUserProfile(username)
                        _chatState.value = _chatState.value.copy(fetchedUser = user)
                    }
                    _chatState.value =
                        _chatState.value.copy(usernameAvailable = !available)
                } catch (e: Exception) {
                    _chatState.value = _chatState.value.copy(usernameAvailable = false)
                }
            }
            _chatState.value = _chatState.value.copy(checkingUsername = false)
        }
    }

    fun setUser(username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val user: UserResponse = authRepository.getUserProfile(username)
                val userList = _chatState.value.userList.toMutableMap()
                val userListModel = userList[username]!!
                userList[username] = userListModel.copy(
                    username = username,
                    profileUrl = user.profileUrl ?: "",
                    name = user.displayName ?: "",
                )
                _chatState.value = _chatState.value.copy(userList = userList, fetchedUser = user)
                AppDatabase.db?.chatListRepository()?.save(userListModel)
            } catch (e: Exception) {
                Log.e("RetrofitError", "Unable to fetch user. ${e.message.toString()}")
            }
        }
    }

    fun onSearchChange(username: String) {
        runnable?.let {
            handler?.removeCallbacks(it)
        }

        // Schedule a new runnable after the cooldown period
        runnable = Runnable {
            // Call your API function here
            checkUsernameAvailability(username)
        }

        handler?.postDelayed(runnable!!, apiCooldownMillis)
    }

    fun clearFetchedUser() {
        _chatState.value = _chatState.value.copy(fetchedUser = null)
    }
}