package com.teknophase.chat.viewmodel

import androidx.lifecycle.ViewModel
import com.teknophase.chat.data.state.RegisterState
import com.teknophase.chat.network.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private var _registerState: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState

    fun goToNextPage() {
        if (_registerState.value.registrationPage < 4)
            _registerState.value =
                _registerState.value.copy(registrationPage = registerState.value.registrationPage + 1)
        when(_registerState.value.registrationPage) {
            4 -> {
                GlobalScope.launch(Dispatchers.IO) {
                    _registerState.value = _registerState.value.copy(isLoading = true)
                    val username = registerState.value.username.toString()
                    if (username.isNotEmpty()){
                        val availability = authRepository.checkUsernameAvailability(username)
                        if(!availability.body()!!) _registerState.value.copy(registrationPage = registerState.value.registrationPage + 1)
                    }
                    _registerState.value = _registerState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun goToPreviousPage() {
        if (_registerState.value.registrationPage > 1)
            _registerState.value =
                _registerState.value.copy(registrationPage = registerState.value.registrationPage - 1)
    }

    fun changeUsername(username: String) {
        _registerState.value = _registerState.value.copy(username = username)
    }

    fun changePassword(password: String) {
        _registerState.value = _registerState.value.copy(password = password)
    }

    fun changeRepeatPassword(repeatPassword: String) {
        _registerState.value = _registerState.value.copy(repeatPassword = repeatPassword)
    }

    fun changeOTP(otp: String) {
        _registerState.value = _registerState.value.copy(otp = otp)
    }

    fun changeCountryCode(countryCode: String) {
        _registerState.value = _registerState.value.copy(countryCode = countryCode)
    }

    fun changeMobileNumber(mobileNumber: String) {
        _registerState.value = _registerState.value.copy(mobileNumber = mobileNumber)
    }

    fun changeDisplayName(displayName: String) {
        _registerState.value = _registerState.value.copy(displayName = displayName)
    }

    fun changeProfileUrl(profileUrl: String) {
        _registerState.value = _registerState.value.copy(profileUrl = profileUrl)
    }


}