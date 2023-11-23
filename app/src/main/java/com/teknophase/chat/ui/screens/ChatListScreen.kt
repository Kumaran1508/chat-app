package com.teknophase.chat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ChatListScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = navController.currentDestination?.route ?: "No route",
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}