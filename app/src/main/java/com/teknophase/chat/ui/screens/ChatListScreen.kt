package com.teknophase.chat.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.teknophase.chat.R
import com.teknophase.chat.navigation.AppNavRoutes
import com.teknophase.chat.network.SocketManager
import com.teknophase.chat.ui.chatlist.ChatListItem
import com.teknophase.chat.ui.common.AppTextField
import com.teknophase.chat.ui.common.InfoBanner
import com.teknophase.chat.ui.common.PrimaryButton
import com.teknophase.chat.ui.constants.padding_small
import com.teknophase.chat.ui.theme.errorRed
import com.teknophase.chat.viewmodel.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatListScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            val state = homeViewModel.chatState.collectAsState()
            val isConnected = SocketManager.isConnected.collectAsState()
            val usernameAvailable = state.value.usernameAvailable

            //Temporary
            var user by remember {
                mutableStateOf("")
            }
            AppTextField(
                title = "",
                value = user,
                onValueChange = {
                    user = it
                    homeViewModel.onSearchChange(it)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (usernameAvailable) navController.navigate(
                        route = AppNavRoutes.CHAT.route.replace("{username}", user),
                    )
                })
            )
            Row {
                PrimaryButton(
                    text = "Go",
                    modifier = Modifier.padding(padding_small),
                    enabled = usernameAvailable
                ) {
                    navController.navigate(
                        route = AppNavRoutes.CHAT.route.replace(
                            "{username}",
                            user
                        )
                    )
                }
            }
            // Temporary Ends here

            if (!isConnected.value) InfoBanner(info = stringResource(R.string.offline), errorRed)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(state.value.userList.count()) { index ->
                    val chatListItem = state.value.userList.values.toList()[index]
                    ChatListItem(
                        name = chatListItem.name,
                        message = chatListItem.message,
                        time = chatListItem.time,
                        profileUrl = chatListItem.profileUrl,
                        unread = chatListItem.unread,
                        modifier = Modifier
                            .animateItemPlacement(
                                tween(600)
                            )
                            .clickable {
                                homeViewModel.setUser(chatListItem.username)
                                homeViewModel.markRead(chatListItem.username)
                                navController.navigate(
                                    route = AppNavRoutes.CHAT.route.replace(
                                        "{username}",
                                        chatListItem.username
                                    )
                                )
                            }
                    )
                }
            }
        }
    }
}