package com.teknophase.chat.util

import android.util.Log

fun getFormattedPhoneNumber(code: String, number: String): String {
    val formattedCode = if (code.length > 1) "(${code.substring(1)})" else code
    val formattedNumberWithoutCode =
        if (number.length > 7) "${number.substring(0, 7)}-${number.substring(7)}" else number
    Log.d("FormattedMobile","$formattedCode$formattedNumberWithoutCode")
    return "$formattedCode$formattedNumberWithoutCode"
}