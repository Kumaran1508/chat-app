package com.teknophase.chat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.teknophase.chat.data.model.ChatListModel
import com.teknophase.chat.data.model.DestinationTypeDeserializer
import com.teknophase.chat.data.model.Message
import com.teknophase.chat.data.model.MessageDeliveredModel
import com.teknophase.chat.data.model.MessageDestinationType
import com.teknophase.chat.data.model.MessageReadModel
import com.teknophase.chat.data.model.MessageStatus
import com.teknophase.chat.data.model.MessageStatusDeserializer
import com.teknophase.chat.data.model.MessageType
import com.teknophase.chat.data.model.MessageTypeDeserializer
import com.teknophase.chat.data.request.MessageRequest
import com.teknophase.chat.data.response.MessageAcknowledgeResponse
import com.teknophase.chat.data.state.ChatState
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val messageRepository: MessageRepository) :
    ViewModel() {
    private var _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.addOnChatListener(chatListener)
            messageRepository.addOnAcknowledgeListener(acknowledgeListener)
            messageRepository.addOnDeliveredListener(deliveredListener)
            messageRepository.addOnReadListener(readListener)
        }
    }


    private val chatListener = Emitter.Listener { _message ->
        Log.d(SOCKET_CHAT_MESSAGE, _message[0].toString())
        try {
            val message: Message = GsonBuilder()
                .registerTypeAdapter(
                    MessageDestinationType::class.java,
                    DestinationTypeDeserializer()
                )
                .registerTypeAdapter(
                    MessageType::class.java,
                    MessageTypeDeserializer()
                )
                .registerTypeAdapter(
                    MessageStatus::class.java,
                    MessageStatusDeserializer()
                )
                .create()
                .fromJson(_message[0].toString(), Message::class.java)
            val userList = _chatState.value.userList.toMutableMap()

            if (!userList.containsKey(message.source)) {
                userList[message.source] = ChatListModel(
                    name = message.source,
                    message = message.content,
                    time = getFormattedTimeForMessage(message.sentAt),
                    profileUrl = "",
                    unread = 1
                )
            } else {
                val count = userList[message.source]!!.unread
                userList[message.source] = userList[message.source]!!.copy(
                    unread = if (count != null) count + 1 else 1,
                    message = message.content,
                    time = getFormattedTimeForMessage(message.sentAt)
                )
            }

            _chatState.value =
                _chatState.value.copy(
                    messages = _chatState.value.messages.plus(message),
                    userList = userList
                )

            viewModelScope.launch(Dispatchers.IO) {
                val deliveredModel = MessageDeliveredModel(
                    messageId = message.messageId,
                    source = message.source,
                    acknowledgedBy = AuthState.getUser()!!.username
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
                if (it.messageId == acknowledgeResponse.requestId) it.copy(
                    messageId = acknowledgeResponse.messageId,
                    messageStatus = MessageStatus.SENT
                )
                else it
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
                if (it.messageId == deliveredModel.messageId) it.copy(
                    messageStatus = MessageStatus.DELIVERED,
                    delivered = true,
                    receivedAt = deliveredModel.deliveredAt
                )
                else it
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
                if (it.messageId == readModel.messageId) it.copy(
                    messageStatus = MessageStatus.READ,
                    read = true,
                    readAt = readModel.readAt
                )
                else it
            }

            _chatState.value =
                _chatState.value.copy(
                    messages = messages
                )
        } catch (e: Exception) {
            Log.e("ReadParseError", "Unable to parse Read message")
        }
    }

    fun onTextChange(text: String) {
        _chatState.value = _chatState.value.copy(clipBoard = text)
    }

    fun onSend(username: String) {
        if (chatState.value.clipBoard.isEmpty()) return
        val messageRequest = MessageRequest(
            sender = AuthState.getUser()?.username.toString(),
            receiver = username,
            content = chatState.value.clipBoard
        )

        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.sendMessage(messageRequest)
        }

        _chatState.value = _chatState.value.copy(
            messages = _chatState.value.messages.plusElement(
                Message(
                    messageId = messageRequest.requestId,
                    source = messageRequest.sender,
                    destination = messageRequest.receiver,
                    content = messageRequest.content,
                    destinationType = MessageDestinationType.values()[messageRequest.destinationType], // Todo: Make this future proof by adding default
                    sentAt = messageRequest.sentAt,
                    messageType = MessageType.values()[messageRequest.messageType],
                    hasAttachment = messageRequest.hasAttachment
                )
            ),
            clipBoard = ""
        )
    }

    fun markRead(username: String) {
        val userList = _chatState.value.userList.toMutableMap()
        userList[username] = userList[username]!!.copy(unread = null)
        _chatState.value = _chatState.value.copy(
            userList = userList
        )

        viewModelScope.launch {
            val userMessages = chatState.value.messages.filter {
                it.source == username &&
                        it.read == false
            }
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


}