package com.teknophase.chat.data.request

import com.teknophase.chat.data.model.MessageDestinationType
import com.teknophase.chat.data.model.MessageType
import java.util.Date

data class MessageRequest(
    val sender: String,
    val receiver: String,
    val content: String,
    val sentAt: Date = Date(),
    val messageType: Int = MessageType.DEFAULT.ordinal,
    val destinationType: Int = MessageDestinationType.DM.ordinal,
    val hasAttachment: Boolean? = false
)
