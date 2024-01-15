package com.teknophase.chat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.teknophase.chat.ui.chatlist.ChatListItemPreview

@Composable
fun ChatListScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(12) {
                ChatListItemPreview()
            }
        }
    }
}