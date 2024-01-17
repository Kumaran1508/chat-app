package com.teknophase.chat.data.state

data class ChatHeaderState(
    val receiverUsername: String = "loading...",
    val receiverId: String = "loading...",
    val about: String = "loading...",
    val profileUrl: String = ""
)
