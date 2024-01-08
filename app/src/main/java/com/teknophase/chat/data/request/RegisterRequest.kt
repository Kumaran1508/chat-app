package com.teknophase.chat.data.request

data class RegisterRequest(
    val mobileNumber: String,
    val countryCode: String,
    val username: String,
    val password: String,
    val displayName: String? = null,
    val profileUrl: String? = null
)
