package com.teknophase.chat.ui.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teknophase.chat.ui.theme.ChatTheme
import com.teknophase.chat.ui.theme.PrimaryBlue
import com.teknophase.chat.ui.theme.errorRed
import com.teknophase.chat.ui.theme.onBackground

@Composable
fun AppTextField(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (value: String) -> Unit,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 24.dp, bottom = 4.dp),
            style = TextStyle(
                fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
        )
        TextField(
            value = value,
            placeholder = {
                if (placeholder != null) {
                    Text(text = placeholder)
                }
            },
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor=MaterialTheme.colorScheme.surface,
                unfocusedContainerColor=MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor=MaterialTheme.colorScheme.surface,
            ),
            shape = RoundedCornerShape(56.dp),
            isError = isError,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            readOnly = readOnly,
            enabled = enabled,
            prefix = prefix,
            suffix = suffix,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            interactionSource = interactionSource
        )
        if (errorText != null) {
            Text(
                text = errorText,
                modifier = Modifier.padding(start = 24.dp, top = 4.dp),
                color = errorRed,
                fontSize = 12.sp
            )
        }

    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
fun AppTextFieldPreview() {
    ChatTheme {
        AppTextField(
            title = "Title Example",
            value = "",
            placeholder = "placeholder",
            onValueChange = {},
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Build, contentDescription = "", tint = PrimaryBlue)
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "",
                    tint = onBackground
                )
            },
            errorText = "sample error"
        )
    }
}
