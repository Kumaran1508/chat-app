package com.teknophase.chat.network.repositories

import com.google.gson.Gson
import com.teknophase.chat.data.model.MessageDeliveredModel
import com.teknophase.chat.data.model.MessageReadModel
import com.teknophase.chat.data.request.MessageRequest
import com.teknophase.chat.network.SocketManager
import io.socket.emitter.Emitter

const val SOCKET_CHAT_MESSAGE: String = "chat"
const val SOCKET_MESSAGE: String = "message"
const val SOCKET_CLOSE: String = "close"
const val SOCKET_CHAT_ACKNOWLEDGE: String = "acknowledge"
const val SOCKET_CHAT_DELIVERED: String = "delivered"
const val SOCKET_CHAT_READ: String = "read"

class SocketMessageRepository : MessageRepository {

    private val gson = Gson()
    override suspend fun sendMessage(message: MessageRequest) {
        val socket = SocketManager.getSocket()

        socket.emit(
            SOCKET_CHAT_MESSAGE,
            gson.toJson(message)
        )
    }

    override suspend fun addOnChatListener(onChatMessage: Emitter.Listener) {
        SocketManager.getSocket().on(SOCKET_CHAT_MESSAGE, onChatMessage)
    }

    override suspend fun addOnMessageListener(onMessage: Emitter.Listener) {
        SocketManager.getSocket().on(SOCKET_MESSAGE, onMessage)
    }

    override suspend fun addOnCloseListener(onClose: Emitter.Listener) {
        SocketManager.getSocket().on(SOCKET_CLOSE, onClose)
    }

    override suspend fun addOnAcknowledgeListener(onAcknowledge: Emitter.Listener) {
        SocketManager.getSocket().on(SOCKET_CHAT_ACKNOWLEDGE, onAcknowledge)

    }

    override suspend fun addOnDeliveredListener(onDelivered: Emitter.Listener) {
        SocketManager.getSocket().on(SOCKET_CHAT_DELIVERED, onDelivered)
    }

    override suspend fun onDelivered(deliveredModel: MessageDeliveredModel) {
        SocketManager.getSocket().emit(
            SOCKET_CHAT_DELIVERED,
            gson.toJson(deliveredModel)
        )
    }

    override suspend fun addOnReadListener(onRead: Emitter.Listener) {
        SocketManager.getSocket().on(SOCKET_CHAT_READ, onRead)
    }

    override suspend fun onRead(readModel: MessageReadModel) {
        SocketManager.getSocket().emit(
            SOCKET_CHAT_READ,
            gson.toJson(readModel)
        )
    }

    override suspend fun addOnQueueListener(onQueue: Emitter.Listener) {
        SocketManager.getSocket().on("queue", onQueue)
    }
}