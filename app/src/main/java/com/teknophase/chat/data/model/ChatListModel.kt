package com.teknophase.chat.data.model

data class ChatListModel(
    val name: String,
    val message: String,
    val time: String,
    val profileUrl: String,
    val unread: Int? = null,
    val pinned: Boolean = false
)
