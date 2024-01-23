package com.teknophase.chat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ChatListModel(
    @PrimaryKey val userId: String,
    val username: String,
    val name: String,
    val message: String,
    val time: String,
    val profileUrl: String,
    val unread: Int? = null,
    val pinned: Boolean = false
)
