package com.teknophase.chat.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.teknophase.chat.data.request.AuthRequest
import com.teknophase.chat.network.repositories.AuthRepository
import com.teknophase.chat.providers.AuthState
import com.teknophase.chat.providers.UserMetaData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private var userState: MutableStateFlow<AuthRequest> = MutableStateFlow(AuthRequest("", ""))
    val user: StateFlow<AuthRequest> = userState
    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun onUsernameChange(username: String) {
        userState.value = user.value.copy(username = username)
    }

    fun onPasswordChange(password: String) {
        userState.value = user.value.copy(password = password)
    }

    fun onForgotPasswordClicked() {
        //TODO: To be Implemented
    }

    fun onLoginClicked(context: Context, onLoginSuccess: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val authResponse = authRepository.login(user.value)
                AuthState.loggedIn(
                    context,
                    authResponse.accessToken,
                    UserMetaData(username = user.value.username, userId = authResponse.userId)
                )
                GlobalScope.launch(Dispatchers.Main) {
                    onLoginSuccess()
                }
            } catch (e: Exception) {
                Log.e("LoginError", e.message.toString())
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}

