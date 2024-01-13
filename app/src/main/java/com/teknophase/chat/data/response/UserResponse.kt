package com.teknophase.chat.data.response

data class UserResponse(
    val mobile_number: String,
    val username: String,
    val display_name: String?,
    val profile_url: String?,
    val about: String?
)
