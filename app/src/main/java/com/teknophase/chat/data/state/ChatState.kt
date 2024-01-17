package com.teknophase.chat.data.state

import com.teknophase.chat.data.model.Message
import com.teknophase.chat.data.request.MessageRequest

data class ChatState(
    val messages: List<Message> = emptyList(),
    val sendQueue: List<MessageRequest> = emptyList(),
    val clipBoard: String = ""
)
