package com.teknophase.chat.data.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("profileUrl") val profileUrl: String? = null,
    @SerializedName("about") val about: String? = null,
)