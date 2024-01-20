package com.teknophase.chat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.teknophase.chat.data.state.ChatHeaderState
import com.teknophase.chat.network.repositories.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val messageRepository: MessageRepository) :
    ViewModel() {
    private var _chatHeaderState: MutableStateFlow<ChatHeaderState> =
        MutableStateFlow(ChatHeaderState())
    val chatHeaderState: StateFlow<ChatHeaderState> = _chatHeaderState


    init {
        GlobalScope.launch(Dispatchers.IO) {
            messageRepository.addOnMessageListener { data ->
                onMessage(data)
            }
        }
    }

    private fun onMessage(data: Any) {
        Log.d("socketServer", data.toString())
    }

    fun setHeader(headerState: ChatHeaderState) {
        _chatHeaderState.value = headerState
    }
}