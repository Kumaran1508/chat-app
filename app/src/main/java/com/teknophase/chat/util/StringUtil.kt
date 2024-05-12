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

fun String.getFirstWord(): String {
    val trimmedString = this.trim() // Remove leading and trailing whitespaces
    val firstSpaceIndex = trimmedString.indexOf(' ') // Find the index of the first space
    return if (firstSpaceIndex != -1) {
        trimmedString.substring(0, firstSpaceIndex) // Extract the substring before the first space
    } else {
        trimmedString // If no space found, return the entire string
    }
}