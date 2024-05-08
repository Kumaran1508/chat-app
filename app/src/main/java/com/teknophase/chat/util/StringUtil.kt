package com.teknophase.chat.util

fun String.getFirstLetter(placeHolderChar: String = ""): String {
    return if (isNotEmpty()) {
        this[0].toString()
    } else {
        placeHolderChar
    }
}

fun String.containsAny(strings: List<String>): Boolean {
    for (str in strings) {
        if (this.contains(str)) {
            return true
        }
    }
    return false
}