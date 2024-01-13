package com.teknophase.chat.data.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("mobile_number") val mobileNumber: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("profile_url") val profileUrl: String? = null
)
