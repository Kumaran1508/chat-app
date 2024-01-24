package com.teknophase.chat.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.teknophase.chat.ui.constants.padding_small
import com.teknophase.chat.ui.constants.text_small
import com.teknophase.chat.ui.theme.errorRed

@Composable
fun InfoBanner(info: String, backgroundColor: Color = MaterialTheme.colorScheme.primary) {
    Text(
        text = info,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = padding_small),
        color = Color.White,
        fontSize = text_small,
        textAlign = TextAlign.Center
    )
}

@Composable
@Preview()
fun InfoBannerPreview() {
    InfoBanner(info = "Network Error", errorRed)
}