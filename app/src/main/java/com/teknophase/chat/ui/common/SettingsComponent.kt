package com.teknophase.chat.ui.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.teknophase.chat.R
import com.teknophase.chat.ui.constants.size_06
import com.teknophase.chat.ui.constants.size_08
import com.teknophase.chat.ui.constants.size_16
import com.teknophase.chat.ui.constants.size_24
import com.teknophase.chat.ui.constants.size_36
import com.teknophase.chat.ui.constants.size_48
import com.teknophase.chat.ui.constants.text_normal
import com.teknophase.chat.ui.theme.ChatTheme
import com.teknophase.chat.ui.theme.orange

@Composable
fun SettingsComponent(
    @DrawableRes icon: Int,
    color: Color,
    title: String,
    onclick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(size_48)
            .padding(horizontal = size_08)
            .clickable { onclick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier
                    .size(size_36)
                    .clip(RoundedCornerShape(size_36))
                    .background(color.copy(alpha = 0.25f))
                    .padding(size_06),
                tint = color
            )

            Text(
                text = title,
                fontSize = text_normal,
                modifier = Modifier.padding(horizontal = size_16),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = "",
            modifier = Modifier
                .size(size_24),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview()
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SettingsComponentPreview() {
    ChatTheme {
        Column {
            SettingsComponent(icon = R.drawable.icon_earth, color = orange, "Visibility")
            SettingsComponent(icon = R.drawable.icon_profile, color = MaterialTheme.colorScheme.primary, "Profile")
        }
    }
}