package com.teknophase.chat.providers

import android.content.Context

object AuthState {
    private var isLoggedIn: Boolean = false
    private var authToken: String? = null
    private var user: UserMetaData? = null

    fun loggedIn(context: Context, authToken: String, user: UserMetaData) {
        this.authToken = authToken
        this.user = user
        val sharedPreferences = context.getSharedPreferences("chat-auth", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("username", user.username)
            .putString("authToken", authToken)
            .putString("userId", user.userId)
            .apply()
        isLoggedIn = true
    }

    fun isLoggedIn(): Boolean {
        return isLoggedIn
    }

    fun getUser(): UserMetaData? {
        return user
    }

    fun getToken(): String? {
        return authToken
    }

    fun logout(context: Context) {
        isLoggedIn = false
        authToken = null
        user = null
        val sharedPreferences = context.getSharedPreferences("chat-auth", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .remove("username")
            .remove("authToken")
            .remove("userId")
            .apply()
    }
}

data class UserMetaData(
    val username: String,
    val userId: String
)