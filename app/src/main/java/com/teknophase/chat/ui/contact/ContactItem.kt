package com.teknophase.chat.ui.contact

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teknophase.chat.data.model.Contact
import com.teknophase.chat.ui.constants.padding_medium
import com.teknophase.chat.ui.constants.padding_small
import com.teknophase.chat.ui.constants.size_56
import com.teknophase.chat.ui.constants.text_large
import com.teknophase.chat.ui.constants.text_normal
import com.teknophase.chat.ui.theme.ChatTheme
import com.teknophase.chat.ui.theme.orange
import com.teknophase.chat.util.getFirstLetter

@Composable
fun ContactItem(contact: Contact, onclickNumber: (number: String) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding_small)
    ) {
        if (contact.photo != null) {
            Image(
                bitmap = contact.photo.asImageBitmap(),
                contentDescription = "",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(orange.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.getFirstLetter("#").uppercase(),
                    color = orange,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = text_large
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = padding_medium)
        ) {
            if (contact.name.isNotEmpty()) {
                Text(
                    text = contact.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = text_normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            for (number in contact.phoneNumbers) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(size_56)
                        .clickable {
                            onclickNumber(number)
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = number,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Left,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ContactItemPreview() {
    ChatTheme {
        Surface {
            ContactItem(
                contact = Contact(
                    id = "Carri",
                    name = "Kage",
                    phoneNumbers = listOf("+918764876358", "+918764876358", "+918764876358"),
                    photo = null
                )
            )
        }
    }
}