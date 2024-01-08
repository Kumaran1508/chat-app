package com.teknophase.chat.navigation

import com.teknophase.chat.R

enum class BottomNavRoutes(val title: String, val icon: Int, val route: String) {
    CHAT("Chats", R.drawable.icon_chat, "${AppNavRoutes.BOTTOM_NAVIGATION.route}/chat"),
    CALLS("Calls", R.drawable.icon_call, "${AppNavRoutes.BOTTOM_NAVIGATION.route}/call_history"),
    PROFILE("Profile", R.drawable.icon_profile, "${AppNavRoutes.BOTTOM_NAVIGATION.route}/profile")
}

enum class AppNavRoutes(val title: String, val route: String) {
    LOGIN("Login", "login"),
    BOTTOM_NAVIGATION("Home Navigation", "bottom_navigation_home"),
    REGISTER("Register", "register"),
}