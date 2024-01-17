package com.teknophase.chat.network.repositories

import com.teknophase.chat.data.request.MessageRequest
import io.socket.emitter.Emitter

interface MessageRepository {
    suspend fun sendMessage(message: MessageRequest)

    suspend fun addOnChatListener(onChatMessage: Emitter.Listener)

    suspend fun addOnMessageListener(onMessage: (Any) -> Unit)
}