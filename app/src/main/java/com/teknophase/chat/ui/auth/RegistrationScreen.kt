package com.teknophase.chat.ui.auth

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.teknophase.chat.R
import com.teknophase.chat.data.request.AuthRequest
import com.teknophase.chat.data.request.RegisterRequest
import com.teknophase.chat.data.request.UpdateProfileRequest
import com.teknophase.chat.data.response.AuthResponse
import com.teknophase.chat.navigation.BottomNavRoutes
import com.teknophase.chat.network.repositories.AuthRepository
import com.teknophase.chat.ui.common.PrimaryButton
import com.teknophase.chat.ui.common.SecondaryButton
import com.teknophase.chat.ui.constants.FULL_WEIGHT
import com.teknophase.chat.ui.constants.padding_medium
import com.teknophase.chat.ui.constants.padding_small
import com.teknophase.chat.ui.constants.size_05
import com.teknophase.chat.ui.constants.size_08
import com.teknophase.chat.ui.constants.size_100
import com.teknophase.chat.ui.constants.size_50
import com.teknophase.chat.ui.theme.ChatTheme
import com.teknophase.chat.ui.theme.progressBarTrackColor
import com.teknophase.chat.viewmodel.RegisterViewModel

const val MAX_PAGES = 4
const val MIN_PROGRESS = 0.08f

@Composable
fun RegistrationScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val registerState = viewModel.registerState.collectAsState()
    val step = registerState.value.registrationPage
    var animationDirection by remember {
        mutableIntStateOf(1)
    }

    val progress: Float = if (step == 1) {
        MIN_PROGRESS
    } else {
        ((registerState.value.registrationPage - 1).toFloat() / MAX_PAGES)
    }

    val context = LocalContext.current as Activity

    BackHandler {
        if (registerState.value.registrationPage == 1)
            onNavigateBack()     //context.finish()
        else {
            animationDirection = -1
            viewModel.goToPreviousPage()
        }
    }

    DisposableEffect(context) {
        // Lock the screen orientation.
        context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        onDispose {
            // Release the the screen orientation lock.
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(padding_medium)
    ) {
        Row(
            modifier = Modifier
                .padding(top = padding_small)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.step))
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(registerState.value.registrationPage.toString())
                    }
                    append(stringResource(R.string.of_value, MAX_PAGES))
                },
                fontWeight = FontWeight.SemiBold
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .width(size_100)
                    .align(CenterVertically)
                    .height(size_08)
                    .clip(RoundedCornerShape(size_05)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = progressBarTrackColor,
                progress = progress
            )
        }

        Image(
            painter = painterResource(id = R.drawable.pyng_logo_onboarding),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = size_50)
                .align(CenterHorizontally)
        )

        AnimatedContent(
            targetState = registerState.value.registrationPage,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it * animationDirection }
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it * animationDirection }
                )
            }, label = ""
        ) { page ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = CenterHorizontally
            ) {
                when (page) {
                    1 -> SignUpForm1()
                    2 -> SignUpForm2()
                    3 -> SignUpForm3()
                    4 -> SignUpForm4(onNavigate = onNavigate)
                }
            }

        }

        Spacer(modifier = Modifier.weight(FULL_WEIGHT))

        if (registerState.value.registrationPage < MAX_PAGES) {
            if (registerState.value.isLoading) {
                CircularProgressIndicator()
            } else {
                PrimaryButton(
                    text = stringResource(R.string.next),
                    modifier = Modifier
                        .padding(bottom = padding_medium)
                        .fillMaxWidth(),
                    enabled = registerState.value.isValid
                ) {
                    animationDirection = 1
                    viewModel.goToNextPage(context.applicationContext)
                }
            }


            if (registerState.value.registrationPage > 1) SecondaryButton(
                text = stringResource(R.string.back),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                animationDirection = -1
                viewModel.goToPreviousPage()
            }
        } else {
            PrimaryButton(
                text = stringResource(R.string.done),
                modifier = Modifier
                    .padding(bottom = padding_medium)
                    .fillMaxWidth()
            ) {
                viewModel.updateProfile(context.baseContext)
                onNavigate(BottomNavRoutes.CHAT.route)
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    ChatTheme {
        RegistrationScreen(
            viewModel = RegisterViewModel(object : AuthRepository {
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
            onNavigate = {},
            onNavigateBack = {}
        )
    }
}