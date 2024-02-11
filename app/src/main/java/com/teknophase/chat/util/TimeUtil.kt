package com.teknophase.chat.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getFormattedTimeForMessage(date: Date?): String {
    if (date == null) return ""
    val customFormatter = SimpleDateFormat("d MMM yyyy h:mm a", Locale.getDefault())
    return customFormatter.format(date)
}