package com.teknophase.chat.data.response

import java.util.Date

data class UserUpdateResponse(
    val username: String,
    val isOnline: Boolean?,
    val lastOnline: Date?
)
