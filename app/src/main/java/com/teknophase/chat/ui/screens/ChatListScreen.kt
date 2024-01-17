package com.teknophase.chat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.teknophase.chat.navigation.AppNavRoutes
import com.teknophase.chat.network.SocketManager
import com.teknophase.chat.ui.chatlist.ChatListItemPreview
import com.teknophase.chat.ui.common.AppTextField
import com.teknophase.chat.ui.common.PrimaryButton

@Composable
fun ChatListScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            //Temporary
            var user by remember {
                mutableStateOf("")
            }
            AppTextField(
                title = "",
                value = user,
                onValueChange = { user = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            PrimaryButton(text = "Go") {
                navController.navigate(
                    route = AppNavRoutes.CHAT.route.replace("{username}", user),
                )
            }
            PrimaryButton(text = "reconnect") {
                SocketManager.getSocket()
            }
            // Temporary Ends here
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(12) {
                    ChatListItemPreview()
                }
            }
        }
    }
}