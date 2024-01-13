package com.teknophase.chat.data.state

data class RegisterState(
    val countryCode: String? = "",
    val mobileNumber: String? = "",
    val otp: String? = "",
    val otpVerified: Boolean = false,
    val username: String? = "",
    val usernameAvailable: Boolean = true,
    val checkingUsername: Boolean = false,
    val password: String? = "",
    val passwordError: String = "",
    val repeatPassword: String? = "",
    val repeatPasswordError: String = "",
    val registrationPage: Int = 1,
    val displayName: String? = "",
    val profileUrl: String? = "",
    val isLoading: Boolean = false,
    val isValid: Boolean = false,
)
