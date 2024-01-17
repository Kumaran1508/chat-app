package com.teknophase.chat.ui.chat

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.teknophase.chat.R
import com.teknophase.chat.data.state.ChatHeaderState
import com.teknophase.chat.ui.constants.padding_extra_small
import com.teknophase.chat.ui.constants.padding_small
import com.teknophase.chat.ui.constants.size_48
import com.teknophase.chat.ui.constants.size_64
import com.teknophase.chat.ui.constants.text_normal
import com.teknophase.chat.ui.constants.text_small
import com.teknophase.chat.ui.theme.ChatTheme

@Composable
fun ChatHeader(chatHeaderState: ChatHeaderState) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .height(size_64)
            .padding(horizontal = padding_small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (chatHeaderState.profileUrl.isNotEmpty()) {
            AsyncImage(
                model = chatHeaderState.profileUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(size_48)
                    .clip(RoundedCornerShape(size_48))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.pyng_logo_onboarding),
                contentDescription = null,
                modifier = Modifier
                    .size(size_48)
                    .clip(RoundedCornerShape(size_48))
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = padding_small)
        ) {
            Text(
                text = chatHeaderState.receiverUsername,
                fontSize = text_normal,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = chatHeaderState.about,
                fontSize = text_small,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = padding_extra_small)
            )
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ChatHeaderPreview() {
    ChatTheme {
        ChatHeader(
            chatHeaderState = ChatHeaderState(
                receiverUsername = "John Doe",
                about = "developer",
                profileUrl = ""
            )
        )
    }
}