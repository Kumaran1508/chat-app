package com.teknophase.chat.data.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import java.util.Date

data class Message(
    @SerializedName("messageId", alternate = ["_id"]) val messageId: String,
    @SerializedName("source") val source: String,
    @SerializedName("destination") val destination: String,
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
    @SerializedName("isForwarded") val isForwarded: Boolean? = false,
    @SerializedName("messageStatus") val messageStatus: MessageStatus? = MessageStatus.QUEUED
)

class DestinationTypeDeserializer : JsonDeserializer<MessageDestinationType> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MessageDestinationType {
        val ordinal = json?.asInt ?: throw JsonParseException("Invalid ordinal value")
        return MessageDestinationType.values()
            .getOrElse(ordinal) { throw JsonParseException("Invalid ordinal value") }
    }
}

class MessageTypeDeserializer : JsonDeserializer<MessageType> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MessageType {
        val ordinal = json?.asInt ?: throw JsonParseException("Invalid ordinal value")
        return MessageType.values()
            .getOrElse(ordinal) { throw JsonParseException("Invalid ordinal value") }
    }
}

class MessageStatusDeserializer : JsonDeserializer<MessageStatus> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MessageStatus {
        val ordinal = json?.asInt ?: throw JsonParseException("Invalid ordinal value")
        return MessageStatus.values()
            .getOrElse(ordinal) { throw JsonParseException("Invalid ordinal value") }
    }
}
