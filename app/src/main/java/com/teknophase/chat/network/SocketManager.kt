package com.teknophase.chat.network

import com.teknophase.chat.providers.AuthState
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.emitter.Emitter
import okhttp3.Request
import java.net.URISyntaxException

object SocketManager {

    private const val webSocketUrl: String = "ws://192.168.29.84:3000/"

    private var socket: Socket? = null

    fun getSocket(): Socket {
        val id = socket?.id()
        if (socket == null || id == null) {
            try {
                // Create connection options
                val options = IO.Options()

                // Add headers to the options
                val headers = HashMap<String, List<String>>()
                headers["Authorization"] = listOf("Bearer ${AuthState.getToken()}")
//                headers["Authorization"] = "Bearer ${AuthState.getToken()}"

                options.extraHeaders = headers

                // Connect to the Socket.IO server with custom options
                socket = IO.socket(webSocketUrl, options)
                socket?.connect()?.emit("user", AuthState.getUser()?.username)
            } catch (e: URISyntaxException) {
                throw RuntimeException(e)
            }
        }
        return socket!!
    }
}

// Todo: Not needed anymore?
//class CustomSocket(val io: Manager, val nsp: String, val opts: Manager.Options) :
//    Socket(io, nsp, opts) {
//    override fun emit(event: String?, vararg args: Any?): Emitter {
//        val requestBuilder = Request.Builder().url(nsp)
//        requestBuilder.addHeader("Authorization", "Bearer ${AuthState.getToken()}")
//        opts.callFactory.newCall(requestBuilder.build())
//        return super.emit(event, *args)
//    }
//}