package com.teknophase.chat.ui.auth

import android.content.res.Configuration
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teknophase.chat.R
import com.teknophase.chat.ui.common.AppTextField
import com.teknophase.chat.ui.common.PrimaryButton
import com.teknophase.chat.ui.common.SecondaryButton
import com.teknophase.chat.ui.theme.ChatTheme
import com.teknophase.chat.ui.theme.progressBarTrackColor

@Composable
fun RegistrationScreen() {
    var step by remember {
        mutableIntStateOf(1)
    }
    var progress by remember {
        mutableFloatStateOf(0.08f)
    }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = buildAnnotatedString {
                append("Step ")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(step.toString())
                }
                append(" of 4")
            },
                fontWeight = FontWeight.SemiBold
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .width(100.dp)
                    .align(CenterVertically)
                    .height(8.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = progressBarTrackColor,
                progress = progress
            )
        }

        Image(
            painter = painterResource(id = R.drawable.pyng_logo_onboarding),
            contentDescription = "image description",
            modifier = Modifier
                .padding(vertical = 100.dp)
                .align(CenterHorizontally)
        )

        Column(modifier = Modifier.align(CenterHorizontally)) {
            when(step) {
                1 -> SignUpForm1()
                2 -> SignUpForm2()
                3 -> SignUpForm3()
                4 -> SignUpForm4()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if(step<4) {
            PrimaryButton(
                text = "Next",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                step++
                progress = (step-1)*0.25f
            }

            SecondaryButton(
                text = "Back",
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if(step==1) {
                    // TODO: Navigate to Login screen
                } else {
                    step--
                    if(step==1) 0.08f else (step-1)*0.25f
                }
            }
        } else {
            PrimaryButton(
                text = "Done",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) { }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RegistrationScreenPreview(){
    ChatTheme {
        RegistrationScreen()
    }
}