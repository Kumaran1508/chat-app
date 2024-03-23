package com.teknophase.chat.data.response

import java.util.Date

data class UserResponse(
    val id: String,
    val mobileNumber: String,
    val username: String,
    val displayName: String?,
    val profileUrl: String?,
    val about: String?,
    val isOnline: Boolean?,
    val lastOnline: Date?
)