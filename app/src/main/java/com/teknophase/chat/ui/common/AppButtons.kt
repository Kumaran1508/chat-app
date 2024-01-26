package com.teknophase.chat.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teknophase.chat.ui.constants.size_48
import com.teknophase.chat.ui.theme.ChatTheme

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(size_48),
        modifier = modifier.height(size_48),
        enabled = enabled,
        elevation = elevation,
        contentPadding = contentPadding,
        interactionSource = interactionSource

    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(48.dp),
        modifier = modifier.height(48.dp),
        enabled = enabled,
        elevation = elevation,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun ButtonPreviews() {
    ChatTheme {
        Column {
            PrimaryButton(
                text = "Primary",
                modifier = Modifier.fillMaxWidth()
            ) {}

            SecondaryButton(
                text = "Secondary",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {}
        }
    }
}