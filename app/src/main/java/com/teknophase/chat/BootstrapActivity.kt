package com.teknophase.chat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.teknophase.chat.ui.common.AppDialog
import com.teknophase.chat.ui.theme.ChatTheme
import com.teknophase.chat.viewmodel.BootstrapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootstrapActivity : ComponentActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // 3 seconds

    @Inject
    lateinit var viewModel: BootstrapViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatTheme {
                val state = viewModel.state.collectAsState()

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.pyng_logo_onboarding),
                            contentDescription = null
                        )

                        if (state.value.isLoading) CircularProgressIndicator()

                    }

                    if (state.value.isUpdateRequired) AppDialog(
                        title = "Update App",
                        primaryText = "Close",
                        description = "Your app is outdated. Please update the app to continue."
                    ) {
                        finish()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.initialize(applicationContext)
            if (!viewModel.isUpdateRequired()) {
                Handler(mainLooper).postDelayed({
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, SPLASH_TIME_OUT)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatTheme {
        Greeting("Android")
    }
}