package com.teknophase.chat.network.repositories

import com.google.gson.Gson
import com.teknophase.chat.data.request.MessageRequest
import com.teknophase.chat.network.SocketManager
import io.socket.emitter.Emitter

const val SOCKET_CHAT_MESSAGE: String = "chat"
const val SOCKET_MESSAGE: String = "message"

class SocketMessageRepository : MessageRepository {
    override suspend fun sendMessage(message: MessageRequest) {
        val socket = SocketManager.getSocket()

        socket.emit(
            SOCKET_CHAT_MESSAGE,
            Gson().toJson(message)
        )
    }

    override suspend fun addOnChatListener(onChatMessage: Emitter.Listener) {
        val socket = SocketManager.getSocket()
        socket.on(SOCKET_CHAT_MESSAGE, onChatMessage)
    }

    override suspend fun addOnMessageListener(onMessage: (Any) -> Unit) {
        SocketManager.getSocket().on(SOCKET_MESSAGE) {
            onMessage(it)
        }
    }
}