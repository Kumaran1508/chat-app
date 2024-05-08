package com.teknophase.chat.data.model

import android.graphics.Bitmap

data class Contact(
    val id: String,
    val name: String,
    val phoneNumbers: List<String>,
    val photo: Bitmap?
)