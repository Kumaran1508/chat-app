package com.teknophase.chat.network.repositories

import com.teknophase.chat.data.model.MessageDeliveredModel
import com.teknophase.chat.data.model.MessageReadModel
import com.teknophase.chat.data.request.MessageRequest
import io.socket.emitter.Emitter

interface MessageRepository {
    suspend fun sendMessage(message: MessageRequest)

    suspend fun addOnChatListener(onChatMessage: Emitter.Listener)

    suspend fun addOnMessageListener(onMessage: Emitter.Listener)
    suspend fun addOnCloseListener(onClose: Emitter.Listener)
    suspend fun addOnAcknowledgeListener(onAcknowledge: Emitter.Listener)
    suspend fun addOnReadListener(onRead: Emitter.Listener)
    suspend fun addOnDeliveredListener(onDelivered: Emitter.Listener)
    suspend fun onDelivered(deliveredModel: MessageDeliveredModel)
    suspend fun onRead(readModel: MessageReadModel)
}