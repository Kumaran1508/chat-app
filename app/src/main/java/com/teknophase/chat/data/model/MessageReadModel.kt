package com.teknophase.chat.data.model

import java.util.Date

data class MessageReadModel(
    val messageId: String,
    val source: String,
    val acknowledgedBy: String,
    val readAt: Date = Date()
)