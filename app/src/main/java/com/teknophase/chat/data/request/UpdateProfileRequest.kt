package com.teknophase.chat.data.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("profile_url") val profileUrl: String? = null,
    @SerializedName("about") val about: String? = null,
)