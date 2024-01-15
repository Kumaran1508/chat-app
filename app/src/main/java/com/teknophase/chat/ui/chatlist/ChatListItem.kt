package com.teknophase.chat.ui.chatlist

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.teknophase.chat.R
import com.teknophase.chat.ui.constants.padding_small
import com.teknophase.chat.ui.constants.size_20
import com.teknophase.chat.ui.constants.size_48
import com.teknophase.chat.ui.constants.text_normal
import com.teknophase.chat.ui.constants.text_small
import com.teknophase.chat.ui.theme.ChatTheme

@Composable
fun ChatListItem(
    name: String,
    message: String,
    time: String,
    profileUrl: String,
    unread: Int? = null,
    pinned: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(all = padding_small)
            .height(size_48)
    ) {

        if (profileUrl.isNotEmpty()) {
            AsyncImage(
                model = profileUrl,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding_small)
                .align(Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = text_normal
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pinned) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_pin),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (unread != null) {
                        Text(
                            text = unread.toString(),
                            fontSize = text_small,
                            modifier = Modifier
                                .padding(horizontal = padding_small)
                                .clip(RoundedCornerShape(size_48))
                                .background(MaterialTheme.colorScheme.primary)
                                .defaultMinSize(minWidth = size_20, minHeight = size_20)
                                .padding(top = 1.dp),
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = message,
                    fontSize = text_small,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = time,
                    fontSize = text_small,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Preview(showBackground = true)
fun ChatListItemPreview() {
    ChatTheme {
        ChatListItem(
            name = "Kumaran",
            message = "Hello",
            time = "Just now",
            unread = 2,
            pinned = true,
            profileUrl = "https://media.licdn.com/dms/image/C5603AQHcOpTM-upF_w/profile-displayphoto-shrink_400_400/0/1639292656997?e=1710979200&v=beta&t=52vJ9TMqZ_ZtFo_m4nYHhNqyKIOcoRbLAAd88rKMHsY"
        )
    }
}