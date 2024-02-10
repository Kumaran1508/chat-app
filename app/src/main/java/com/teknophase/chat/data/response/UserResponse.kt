package com.teknophase.chat.data.response

data class UserResponse(
    val id: String,
    val mobileNumber: String,
    val username: String,
    val displayName: String?,
    val profileUrl: String?,
    val about: String?
)
