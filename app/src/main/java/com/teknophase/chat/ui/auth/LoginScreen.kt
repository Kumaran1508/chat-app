package com.teknophase.chat.ui.auth

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.teknophase.chat.R
import com.teknophase.chat.navigation.AppNavRoutes
import com.teknophase.chat.navigation.BottomNavRoutes
import com.teknophase.chat.ui.common.AppTextField
import com.teknophase.chat.ui.common.PrimaryButton
import com.teknophase.chat.ui.constants.CREATE_ACCOUNT_TAG
import com.teknophase.chat.ui.constants.NOT_IMPLEMENTED
import com.teknophase.chat.ui.constants.form_field_width
import com.teknophase.chat.ui.constants.padding_extra_large
import com.teknophase.chat.ui.constants.padding_large
import com.teknophase.chat.ui.constants.padding_medium
import com.teknophase.chat.ui.constants.size_100
import com.teknophase.chat.ui.constants.text_normal
import com.teknophase.chat.ui.constants.text_small
import com.teknophase.chat.ui.theme.ChatTheme
import com.teknophase.chat.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {

    var passwordToggle by remember {
        mutableStateOf(false)
    }
    val user = loginViewModel.user.collectAsState()
    val context = LocalContext.current

    val registerAnnotation = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurface
            )
        ) {
            append(text = stringResource(R.string.no_account))
        }
        pushStringAnnotation(
            tag = CREATE_ACCOUNT_TAG,
            annotation = AppNavRoutes.REGISTER.route
        )
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            )
        ) {
            append(text = stringResource(R.string.create_account))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.pyng_logo_onboarding),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = size_100)
                .align(CenterHorizontally)
        )

        AppTextField(
            title = stringResource(R.string.title_username),
            value = user.value.username,
            onValueChange = {
                loginViewModel.onUsernameChange(it)
            },
            modifier = Modifier
                .align(CenterHorizontally),
            placeholder = stringResource(R.string.type_something),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_username),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = padding_medium)
                )
            }
        )

        AppTextField(
            title = stringResource(R.string.title_password),
            value = user.value.password,
            onValueChange = {
                loginViewModel.onPasswordChange(it)
            },
            modifier = Modifier
                .padding(top = padding_large)
                .align(CenterHorizontally),
            placeholder = stringResource(id = R.string.type_something),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_password),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    painter = if (passwordToggle) {
                        painterResource(id = R.drawable.icon_invisible)
                    } else {
                        painterResource(id = R.drawable.icon_visible)
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable {
                        passwordToggle = !passwordToggle
                    }
                )
            },
            visualTransformation = if (!passwordToggle) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        )

        ClickableText(
            text = AnnotatedString(stringResource(R.string.forgot_password)),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = text_normal
            ),
            modifier = Modifier
                .padding(top = padding_large)
                .align(CenterHorizontally),
            onClick = { Toast.makeText(context, NOT_IMPLEMENTED, LENGTH_SHORT).show() },
        )

        Spacer(modifier = Modifier.weight(1f))

        ClickableText(
            text = registerAnnotation,
            modifier = Modifier
                .padding(vertical = padding_extra_large)
                .align(CenterHorizontally),
            style = TextStyle(
                fontSize = text_small
            ),
            onClick = {
                registerAnnotation.getStringAnnotations(
                    tag = CREATE_ACCOUNT_TAG,
                    start = it,
                    end = it
                ).firstOrNull()
                    ?.let {
                        onNavigate(AppNavRoutes.REGISTER.route)
                    }
            }
        )

        PrimaryButton(
            text = stringResource(R.string.login),
            modifier = Modifier
                .width(form_field_width)
                .padding(bottom = padding_extra_large)
                .align(CenterHorizontally)
        ) {
            onNavigate(BottomNavRoutes.CALLS.route)
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LoginFormPreview() {
    ChatTheme {
        LoginScreen(
            loginViewModel = LoginViewModel(),
            onNavigate = {}
        )
    }
}