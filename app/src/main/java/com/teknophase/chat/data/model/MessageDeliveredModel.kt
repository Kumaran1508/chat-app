package com.teknophase.chat.data.model

import java.util.Date

data class MessageDeliveredModel(
    val messageId: String,
    val source: String,
    val acknowledgedBy: String,
    val deliveredAt: Date = Date()
)