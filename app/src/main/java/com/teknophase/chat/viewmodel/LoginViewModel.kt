package com.teknophase.chat.viewmodel

import androidx.lifecycle.ViewModel
import com.teknophase.chat.data.request.AuthRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private var userState: MutableStateFlow<AuthRequest> = MutableStateFlow(AuthRequest("", ""))
    val user: StateFlow<AuthRequest> = userState
    fun onUsernameChange(username: String) {
        userState.value = user.value.copy(username = username)
    }

    fun onPasswordChange(password: String) {
        userState.value = user.value.copy(password = password)
    }

    fun onForgotPasswordClicked() {
        //TODO: To be Implemented
    }
}

