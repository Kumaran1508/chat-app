package com.teknophase.chat.data.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("profileUrl") val profileUrl: String? = null
)
