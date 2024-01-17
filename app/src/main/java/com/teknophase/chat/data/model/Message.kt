package com.teknophase.chat.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Message(
    @SerializedName("messageId") val messageId: String,
    @SerializedName("sender") val source: String,
    @SerializedName("receiver") val destination: String,
    @SerializedName("content") val content: String,
    @SerializedName("destinationType") val destinationType: MessageDestinationType,
    @SerializedName("sentAt") val sentAt: Date,
    @SerializedName("messageType") val messageType: MessageType = MessageType.DEFAULT,
    @SerializedName("delivered") val delivered: Boolean? = false,
    @SerializedName("read") val read: Boolean? = false,
    @SerializedName("hasAttachment") val hasAttachment: Boolean? = false,
    @SerializedName("receivedAt") val receivedAt: Date? = null,
    @SerializedName("readAt") val readAt: Date? = null,
    @SerializedName("edited") val edited: Boolean? = false,
    @SerializedName("attachmentId") val attachmentId: String? = null,
    @SerializedName("isForwarded") val isForwarded: Boolean? = false
)
