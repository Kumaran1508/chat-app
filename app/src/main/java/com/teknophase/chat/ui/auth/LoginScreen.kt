package com.teknophase.chat.ui.auth

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teknophase.chat.R
import com.teknophase.chat.ui.common.AppTextField
import com.teknophase.chat.ui.common.PrimaryButton
import com.teknophase.chat.ui.theme.ChatTheme

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.pyng_logo_onboarding),
            contentDescription = "image description",
            modifier = Modifier
                .padding(vertical = 100.dp)
                .align(CenterHorizontally)
        )

        AppTextField(
            title = "Username",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .align(CenterHorizontally),
            placeholder = "Type Something...",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_username),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        )

        AppTextField(
            title = "Password",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .padding(top = 24.dp)
                .align(CenterHorizontally),
            placeholder = "Type Something...",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_password),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_visible),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            visualTransformation = PasswordVisualTransformation()
        )

        ClickableText(
            text = AnnotatedString("Forgot Password?"),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .padding(top = 24.dp)
                .align(CenterHorizontally),
            onClick = {},
        )

        Spacer(modifier = Modifier.weight(1f))

        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface
                )
                ) {
                    append("Do not have a account? ")
                }
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )){
                    append(text = "Create Account")
                }
            },
            modifier = Modifier
                .padding(vertical = 32.dp)
                .align(CenterHorizontally),
            style = TextStyle(
                fontSize = 12.sp
            ),
            onClick = {}
        )

        PrimaryButton(
            text = "Login",
            modifier = Modifier
                .width(275.dp)
                .padding(bottom = 32.dp)
                .align(CenterHorizontally)
        ) {

        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LoginFormPreview(){
    ChatTheme {
        LoginScreen()
    }
}