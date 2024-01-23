package com.teknophase.chat.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
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
import com.teknophase.chat.navigation.AppNavRoutes
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
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
    private val apiCooldownMillis = 1000L  // Set your cooldown time in milliseconds
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
        }
    }


    private val chatListener = Emitter.Listener { _message ->
        Log.d(SOCKET_CHAT_MESSAGE, _message[0].toString())
        try {
            GlobalScope.launch(Dispatchers.IO) {
                val message: Message = gson
                    .fromJson(_message[0].toString(), Message::class.java)
                val userList = _chatState.value.userList.toMutableMap()

                if (!userList.containsKey(message.source)) {
                    val user = authRepository.getUserProfile(message.source)
                    val chatListModel = createChatListModel(message)
                    userList[message.source] = chatListModel
                    AppDatabase.db?.chatListRepository()?.save(chatListModel)
                } else {
                    val count = userList[message.source]!!.unread
                    val chatListModel = userList[message.source]!!.copy(
                        unread = if (count != null) count + 1 else 1,
                        message = message.content,
                        time = getFormattedTimeForMessage(message.sentAt)
                    )
                    userList[message.source] = chatListModel
                    AppDatabase.db?.chatListRepository()?.update(chatListModel)
                }

                _chatState.value = _chatState.value.copy(
                    messages = _chatState.value.messages.plus(message),
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

            }
        } catch (e: Exception) {
            Log.e("MessageParseError", "Unable to parse received message" + e.message)
            e.printStackTrace()
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
                    // Todo: Update local db
                    GlobalScope.launch(Dispatchers.IO) {
                        AppDatabase.db?.messageRepository()?.delete(it)
                        AppDatabase.db?.messageRepository()?.save(updatedMessage)
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
                    // Todo: Update local db
                    GlobalScope.launch(Dispatchers.IO) {
                        AppDatabase.db?.messageRepository()?.update(updatedMessage)
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
                    // Todo: Update local db
                    GlobalScope.launch(Dispatchers.IO) {
                        AppDatabase.db?.messageRepository()?.update(updatedMessage)
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

        GlobalScope.launch(Dispatchers.IO) {
            if (SocketManager.isConnected.value) {
                messageRepository.sendMessage(messageRequest)
                Log.d("Sent", messageRequest.toString())
            } else Log.e("Socket", "Socket not connected")
        }

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
        _chatState.value = _chatState.value.copy(
            messages = _chatState.value.messages.plusElement(
                message
            ),
            clipBoard = ""
        )
        GlobalScope.launch(Dispatchers.IO) {
            val userList = _chatState.value.userList.toMutableMap()
            if (!userList.containsKey(message.source)) {
                val chatListModel = createChatListModel(message)
                userList[message.source] = chatListModel
                GlobalScope.launch(Dispatchers.IO) {
                    AppDatabase.db?.chatListRepository()?.save(chatListModel)
                }
            }
            AppDatabase.db?.messageRepository()?.save(message)
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
                messageRepository.onRead(
                    MessageReadModel(
                        messageId = message.messageId,
                        source = message.source,
                        acknowledgedBy = AuthState.getUser()!!.username
                    )
                )
            }

        }
    }

    private fun createChatListModel(message: Message): ChatListModel {
        val username =
            if (message.source == AuthState.getUser()!!.username) message.destination else message.source
        return ChatListModel(
            username = username,
            name = username,
            message = message.content,
            time = getFormattedTimeForMessage(message.sentAt),
            profileUrl = "",
            unread = 1,
            userId = UUID.randomUUID().toString()
        )
    }

    private fun checkUsernameAvailability(username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            _chatState.value = _chatState.value.copy(checkingUsername = true)
            if (username.isNotEmpty()) {
                try {
                    val available = authRepository.checkUsernameAvailability(username)
                    if (available) {
                        val user = authRepository.getUserProfile(username)
                        _chatState.value = _chatState.value.copy(fetchedUser = user)
                    }
                    _chatState.value =
                        _chatState.value.copy(usernameAvailable = available)
                } catch (e: Exception) {
                    _chatState.value = _chatState.value.copy(usernameAvailable = false)
                }
            }
            _chatState.value = _chatState.value.copy(checkingUsername = false)
        }
    }

    fun setUser(username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val user = authRepository.getUserProfile(username)
            _chatState.value = _chatState.value.copy(fetchedUser = user)
        }
    }

    fun onNavigateChatScreen(navController: NavController) {

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
}