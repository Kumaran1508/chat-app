package com.teknophase.chat

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.teknophase.chat.navigation.AppNavHost
import com.teknophase.chat.network.SocketManager
import com.teknophase.chat.providers.AuthState
import com.teknophase.chat.providers.UserMetaData
import com.teknophase.chat.ui.theme.ChatTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChatApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAuth(this.baseContext)
        setContent {
            ChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }

    private fun initAuth(context: Context) {
        val sharedPreferences = context.getSharedPreferences("chat-auth", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", null)
        if (authToken != null) {
            val userId = sharedPreferences.getString("userId", null)
            val username = sharedPreferences.getString("username", null)
            AuthState.loggedIn(
                context,
                authToken,
                UserMetaData(username = username.toString(), userId = userId.toString())
            )
            SocketManager.getSocket()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.getSocket().disconnect()
    }
}

