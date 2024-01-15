package com.teknophase.chat.data.model

import java.util.Date

data class Message(
    val messageId: String,
    val source: String,
    val destination: String,
    val destinationType: MessageDestinationType,
    val content: String,
    val sentAt: Date,
    val messageType: MessageType = MessageType.DEFAULT,
    val delivered: Boolean? = false,
    val read: Boolean? = false,
    val hasAttachment: Boolean? = false,
    val receivedAt: Date? = null,
    val readAt: Date? = null,
    val edited: Boolean? = false,
    val attachmentId: String? = null,
    val isForwarded: Boolean? = false
)
