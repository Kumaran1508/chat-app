package com.teknophase.chat.data.state

data class RegisterState(
    val countryCode: String? = "",
    val mobileNumber: String? = "",
    val otp: String? = "",
    val otpVerified: Boolean = false,
    val username: String? = "",
    val password: String? = "",
    val repeatPassword: String? = "",
    val registrationPage: Int = 1,
    val displayName: String? = "",
    val profileUrl: String? = "",
    val isLoading: Boolean = false
)
