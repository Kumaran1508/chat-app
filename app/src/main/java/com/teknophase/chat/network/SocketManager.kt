package com.teknophase.chat.network

import android.util.Log
import com.teknophase.chat.di.WEB_SOCKET_URL
import com.teknophase.chat.providers.AuthState
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SocketManager {

    private var socket: Socket? = null
    private var _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun getSocket(): Socket {
        val id = socket?.id()
        if (socket == null || id == null) {
            try {
                // Create connection options
                val options = IO.Options()

                // Add headers to the options
                val headers = HashMap<String, List<String>>()
                headers["Authorization"] = listOf("Bearer ${AuthState.getToken()}")

                options.extraHeaders = headers

                // Connect to the Socket.IO server with custom options
                socket = IO.socket(WEB_SOCKET_URL, options)
                socket?.on(Socket.EVENT_CONNECT) {
                    _isConnected.value = true
                }
                socket?.on(Socket.EVENT_CONNECT_ERROR) {
                    _isConnected.value = false
                }
                socket?.on(Socket.EVENT_DISCONNECT) {
                    _isConnected.value = false
                }

                socket?.connect()

            } catch (e: Exception) {
                Log.e(
                    "SocketManagerError",
                    "Unable to establish connection ${e.message.toString()}"
                )
                e.printStackTrace()
            }
        }
        return socket!!
    }
}
