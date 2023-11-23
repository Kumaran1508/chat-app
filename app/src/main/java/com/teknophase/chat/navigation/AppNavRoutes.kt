package com.teknophase.chat.navigation

import com.teknophase.chat.R

enum class AppNavRoutes(val title: String,val icon: Int, val route: String) {
    CHAT("Chats", R.drawable.icon_chat, "chat"),
    CALLS("Calls", R.drawable.icon_call, "call_history"),
    PROFILE("Profile",R.drawable.icon_profile ,"profile")
}