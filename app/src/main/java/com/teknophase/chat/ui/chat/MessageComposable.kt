package com.teknophase.chat.ui.chat

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.teknophase.chat.R
import com.teknophase.chat.data.model.Message
import com.teknophase.chat.data.model.MessageDestinationType
import com.teknophase.chat.ui.constants.padding_small
import com.teknophase.chat.ui.constants.size_08
import com.teknophase.chat.ui.constants.size_100
import com.teknophase.chat.ui.constants.size_11
import com.teknophase.chat.ui.constants.size_16
import com.teknophase.chat.ui.constants.size_300
import com.teknophase.chat.ui.constants.text_small
import com.teknophase.chat.ui.theme.ChatTheme
import com.teknophase.chat.util.getFormattedTimeForMessage
import java.util.Date

@Composable
fun MessageComposable(message: Message, isSent: Boolean, modifier: Modifier = Modifier) {
    val backgroundColor =
        if (isSent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(color = backgroundColor, shape = RoundedCornerShape(size_08))
                .padding(padding_small)
                .widthIn(min = size_100, max = size_300)
        ) {
            Text(
                text = message.content,
                style = TextStyle(textAlign = TextAlign.Justify),
                color = if (isSent) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = padding_small)
            )

            Row(modifier = Modifier.align(End)) {
                Text(
                    text = getFormattedTimeForMessage(message.sentAt),
                    fontSize = text_small,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (isSent) {
            val icon =
                if (message.read == true) R.drawable.icon_visible else R.drawable.icon_success
            val color =
                if (message.delivered == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .padding(start = padding_small)
                    .size(size_11)
            )
        }
    }

}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MessagePreview() {
    ChatTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            MessageComposable(
                message = Message(
                    messageId = "avbnnd-ab79-nso8-hn93nd",
                    source = "asdf",
                    destination = "asnd",
                    destinationType = MessageDestinationType.DM,
                    content = "Hello\nHow are you?",
                    sentAt = Date(),
                    delivered = true,
                    read = true
                ),
                isSent = true,
                modifier = Modifier.align(End)
            )
            Spacer(modifier = Modifier.size(size_16))
            MessageComposable(
                message = Message(
                    messageId = "avbnnd-ab79-nso8-hn93nd",
                    source = "asdf",
                    destination = "asnd",
                    destinationType = MessageDestinationType.DM,
                    content = "Hello. This is a very long paragaraph to test the composable for long messages. This message contains almost aa paragraph. If this works fine then the composable is ready.",
                    sentAt = Date()
                ),
                isSent = false
            )
            Spacer(modifier = Modifier.size(size_16))
            MessageComposable(
                message = Message(
                    messageId = "avbnnd-ab79-nso8-hn93nd",
                    source = "asdf",
                    destination = "asnd",
                    destinationType = MessageDestinationType.DM,
                    content = "Okay",
                    sentAt = Date(),
                    delivered = true
                ),
                isSent = true,
                modifier = Modifier.align(End)
            )
        }
    }
}