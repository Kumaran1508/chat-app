package com.teknophase.chat.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: String,
    @SerializedName("mobile_number") val mobileNumber: String,
    val username: String,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("profile_url") val profileUrl: String?,
    val about: String?
)
