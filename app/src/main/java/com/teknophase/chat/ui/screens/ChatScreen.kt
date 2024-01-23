package com.teknophase.chat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.teknophase.chat.R
import com.teknophase.chat.network.SocketManager
import com.teknophase.chat.providers.AuthState
import com.teknophase.chat.ui.chat.ChatHeader
import com.teknophase.chat.ui.chat.MessageComposable
import com.teknophase.chat.ui.common.AppTextField
import com.teknophase.chat.ui.common.InfoBanner
import com.teknophase.chat.ui.constants.padding_small
import com.teknophase.chat.ui.constants.size_48
import com.teknophase.chat.ui.constants.size_64
import com.teknophase.chat.ui.theme.PrimaryBlueVariant
import com.teknophase.chat.ui.theme.errorRed
import com.teknophase.chat.viewmodel.ChatViewModel
import com.teknophase.chat.viewmodel.HomeViewModel

@Composable
fun ChatScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    viewModel: ChatViewModel = hiltViewModel(),
    username: String
) {
    val state = homeViewModel.chatState.collectAsState()
    val headerState = viewModel.chatHeaderState.collectAsState()
    val currentUser = AuthState.getUser()?.username.toString()
    val isConnected = SocketManager.isConnected.collectAsState()
    val user = state.value.fetchedUser



    Column(modifier = Modifier.fillMaxSize()) {
        ChatHeader(
            displayName = if (user?.displayName != null) user.displayName else username,
            profileUrl = user?.profileUrl,
            about = user?.about
        )

        if (!isConnected.value) InfoBanner(info = stringResource(R.string.offline), errorRed)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(padding_small)
        ) {
            items(state.value.messages.size) { index ->
                val message = state.value.messages[index]
                MessageComposable(
                    message = message,
                    isSent = message.source == currentUser,
                    modifier = Modifier.align(Start)
                )
                Spacer(modifier = Modifier.height(padding_small))
            }
        }

        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .height(size_64)
                .padding(horizontal = padding_small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                AppTextField(
                    title = "",
                    value = state.value.clipBoard,
                    onValueChange = {
                        homeViewModel.onMessageChange(it)
                    },
//                leadingIcon = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(painter = painterResource(id = R.drawable.icon_emoji), contentDescription = null)
//                    }
//                },
                    trailingIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_attachment),
                                contentDescription = null
                            )
                        }
                    },
                    maxLines = 1,
                    placeholder = stringResource(id = R.string.type_something),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            IconButton(
                onClick = {
                    if (state.value.clipBoard.isNotEmpty()) homeViewModel.onSend(username)
                }, modifier = Modifier
                    .padding(horizontal = padding_small)
                    .size(size_48)
                    .clip(RoundedCornerShape(size_48))
                    .background(PrimaryBlueVariant)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_send),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }
}