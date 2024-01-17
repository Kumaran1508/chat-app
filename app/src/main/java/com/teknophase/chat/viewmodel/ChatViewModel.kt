package com.teknophase.chat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.teknophase.chat.data.model.Message
import com.teknophase.chat.data.request.MessageRequest
import com.teknophase.chat.data.state.ChatHeaderState
import com.teknophase.chat.data.state.ChatState
import com.teknophase.chat.network.repositories.MessageRepository
import com.teknophase.chat.providers.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val messageRepository: MessageRepository) :
    ViewModel() {
    private var _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState
    private var _chatHeaderState: MutableStateFlow<ChatHeaderState> =
        MutableStateFlow(ChatHeaderState())
    val chatHeaderState: StateFlow<ChatHeaderState> = _chatHeaderState

    private val chatListener = Emitter.Listener { message ->
        Log.d("data", message[0].toString())
        val msg: Message = Gson().fromJson(message[0].toString(), Message::class.java)
        _chatState.value = _chatState.value.copy(messages = _chatState.value.messages.plus(msg))
    }

    fun onTextChange(text: String) {
        _chatState.value = _chatState.value.copy(clipBoard = text)
    }

    init {
        GlobalScope.launch(Dispatchers.IO) {
            messageRepository.addOnChatListener(chatListener)

            messageRepository.addOnMessageListener { data ->
                onMessage(data)
            }
        }
    }

    fun onSend(username: String) {
        if (chatState.value.clipBoard.isEmpty()) return
        val messageRequest = MessageRequest(
            sender = AuthState.getUser()?.username.toString(),
            receiver = username,
            content = chatState.value.clipBoard
        )

        // TODO: Send Message
        GlobalScope.launch(Dispatchers.IO) {
            messageRepository.sendMessage(messageRequest)
        }

        _chatState.value = _chatState.value.copy(
            sendQueue = _chatState.value.sendQueue.plusElement(messageRequest),
            clipBoard = ""
        )
    }

    private fun onMessage(data: Any) {
        Log.d("socketServer", data.toString())
    }

    fun setHeader(headerState: ChatHeaderState) {
        _chatHeaderState.value = headerState
    }
}