package com.teknophase.chat.ui.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.teknophase.chat.ui.constants.padding_medium
import com.teknophase.chat.ui.constants.padding_zero
import com.teknophase.chat.ui.constants.size_150
import com.teknophase.chat.ui.constants.size_16
import com.teknophase.chat.ui.constants.size_250
import com.teknophase.chat.ui.constants.size_300
import com.teknophase.chat.ui.constants.text_large
import com.teknophase.chat.ui.constants.text_normal
import com.teknophase.chat.ui.theme.ChatTheme

@Composable
internal fun AppDialogComposable(
    title: String,
    description: String?,
    primaryText: String,
    secondaryText: String? = null,
    onPrimaryClicked: () -> Unit,
    onSecondaryClicked: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.5f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(min = size_250, max = size_300)
                .heightIn(min = size_150, max = size_300)
                .clip(RoundedCornerShape(size_16))
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    fontSize = text_large,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(top = padding_medium)
                        .padding(horizontal = padding_medium)
                )

                if (description != null) Text(
                    text = description,
                    fontSize = text_normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(padding_medium)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding_medium)
            ) {
                if (secondaryText != null) SecondaryButton(
                    text = secondaryText,
                    modifier = Modifier
                        .weight(1f),
                    onClick = { onSecondaryClicked?.invoke() }
                )
                PrimaryButton(
                    text = primaryText,
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = if (secondaryText != null) padding_medium
                            else padding_zero
                        ),
                    onClick = onPrimaryClicked
                )
            }
        }
    }
}

@Composable
fun AppDialog(
    title: String,
    description: String? = null,
    primaryText: String,
    onPrimaryClicked: () -> Unit
) {
    AppDialogComposable(
        title = title,
        description = description,
        primaryText = primaryText,
        onPrimaryClicked = onPrimaryClicked
    )
}

@Composable
fun AppDialog(
    title: String,
    description: String? = null,
    primaryText: String,
    secondaryText: String,
    onPrimaryClicked: () -> Unit,
    onSecondaryClicked: (() -> Unit)
) {
    AppDialogComposable(
        title = title,
        description = description,
        primaryText = primaryText,
        secondaryText = secondaryText,
        onPrimaryClicked = onPrimaryClicked,
        onSecondaryClicked = onSecondaryClicked
    )
}


@Preview()
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AppDialogPreview() {
    ChatTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            AppDialog(
                "Sample",
                "sample description",
                "OK \n test",
                "Cancel",
                { },
                { }
            )

            AppDialog(title = "Sample 1", primaryText = "OK") {

            }
        }
    }
}