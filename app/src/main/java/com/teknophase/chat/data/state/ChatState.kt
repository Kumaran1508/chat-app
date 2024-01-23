package com.teknophase.chat.data.state

import com.teknophase.chat.data.model.ChatListModel
import com.teknophase.chat.data.model.Message
import com.teknophase.chat.data.response.UserResponse

data class ChatState(
    val messages: List<Message> = emptyList(),
    val userList: Map<String, ChatListModel> = emptyMap(),
    val clipBoard: String = "",
    val isConnected: Boolean = false,
    val checkingUsername: Boolean = false,
    val usernameAvailable: Boolean = false,
    val fetchedUser: UserResponse? = null
)
