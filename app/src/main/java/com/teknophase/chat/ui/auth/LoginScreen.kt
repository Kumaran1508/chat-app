package com.teknophase.chat.ui.auth

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.teknophase.chat.R
import com.teknophase.chat.data.request.AuthRequest
import com.teknophase.chat.data.request.RegisterRequest
import com.teknophase.chat.data.request.UpdateProfileRequest
import com.teknophase.chat.data.response.AuthResponse
import com.teknophase.chat.navigation.AppNavRoutes
import com.teknophase.chat.navigation.BottomNavRoutes
import com.teknophase.chat.network.SocketManager
import com.teknophase.chat.network.repositories.AuthRepository
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
import com.teknophase.chat.viewmodel.USERNAME_MAX_LENGTH
import com.teknophase.chat.viewmodel.USERNAME_MIN_LENGTH
import com.teknophase.chat.viewmodel.passwordRegex

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {

    var passwordToggle by remember {
        mutableStateOf(false)
    }
    val user = loginViewModel.user.collectAsState()
    val isLoading = loginViewModel.isLoading.collectAsState()
    var isUsernameError by remember {
        mutableStateOf(false)
    }
    var isPasswordError by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    BackHandler {
        val activity = context as Activity
        activity.finish()
    }

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
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.pyng_logo_onboarding),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = size_100)
        )

        AppTextField(
            title = stringResource(R.string.title_username),
            value = user.value.username,
            onValueChange = {
                loginViewModel.onUsernameChange(it)
                isUsernameError =
                    it.length !in USERNAME_MIN_LENGTH..USERNAME_MAX_LENGTH
            },
            placeholder = stringResource(R.string.type_something),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_username),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = padding_medium)
                )
            },
            isError = isUsernameError,
            errorText = stringResource(id = R.string.invalid_username)
        )

        AppTextField(
            title = stringResource(R.string.title_password),
            value = user.value.password,
            onValueChange = {
                loginViewModel.onPasswordChange(it)
                isPasswordError = !passwordRegex.matches(it)
            },
            modifier = Modifier
                .padding(top = padding_large),
            placeholder = stringResource(id = R.string.type_something),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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
            },
            isError = isPasswordError,
            errorText = stringResource(id = R.string.invalid_password)
        )

        ClickableText(
            text = AnnotatedString(stringResource(R.string.forgot_password)),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = text_normal
            ),
            modifier = Modifier
                .padding(top = padding_large),
            onClick = { Toast.makeText(context, NOT_IMPLEMENTED, LENGTH_SHORT).show() },
        )

        Spacer(modifier = Modifier.weight(1f))

        ClickableText(
            text = registerAnnotation,
            modifier = Modifier
                .padding(vertical = padding_extra_large),
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

        if (isLoading.value) CircularProgressIndicator()
        else PrimaryButton(
            text = stringResource(R.string.login),
            modifier = Modifier
                .width(form_field_width)
                .padding(bottom = padding_extra_large),
            enabled = !isUsernameError && !isPasswordError
        ) {
            loginViewModel.onLoginClicked(context) {
                SocketManager.getSocket()
                onNavigate(BottomNavRoutes.CHAT.route)
            }
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LoginFormPreview() {
    ChatTheme {
        LoginScreen(
            loginViewModel = LoginViewModel(authRepository = object : AuthRepository {
                override suspend fun login(user: AuthRequest): AuthResponse {
                    TODO("Not yet implemented")
                }

                override suspend fun register(user: RegisterRequest): Boolean {
                    TODO("Not yet implemented")
                }

                override suspend fun checkUsernameAvailability(username: String): Boolean {
                    TODO("Not yet implemented")
                }

                override suspend fun updateUserProfile(profileRequest: UpdateProfileRequest): Boolean {
                    TODO("Not yet implemented")
                }
            }),
            onNavigate = {}
        )
    }
}