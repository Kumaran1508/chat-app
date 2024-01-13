package com.teknophase.chat.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.teknophase.chat.data.request.RegisterRequest
import com.teknophase.chat.data.request.UpdateProfileRequest
import com.teknophase.chat.data.state.RegisterState
import com.teknophase.chat.network.repositories.AuthRepository
import com.teknophase.chat.util.getFormattedPhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

val mobileRegex =
    Regex("^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?\$")
val passwordRegex =
    Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,20}\$")

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private var _registerState: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState
    private val apiCooldownMillis = 1000L  // Set your cooldown time in milliseconds
    private val handler = Looper.myLooper()?.let { Handler(it) }
    private var runnable: Runnable? = null

    fun goToNextPage(context: Context) {
        when (_registerState.value.registrationPage) {
            1 -> {
                _registerState.value =
                    _registerState.value.copy(registrationPage = registerState.value.registrationPage + 1)
            }

            2 -> {
                // TODO: Validate OTP
                _registerState.value =
                    _registerState.value.copy(registrationPage = registerState.value.registrationPage + 1)
            }

            3 -> {
                GlobalScope.launch(Dispatchers.IO) {
                    _registerState.value = _registerState.value.copy(isLoading = true)
                    val username = registerState.value.username.toString()
                    val mobileNumber = getFormattedPhoneNumber(
                        registerState.value.countryCode.toString(),
                        registerState.value.mobileNumber.toString()
                    )
                    val password = registerState.value.password.toString()
                    val displayName = registerState.value.displayName.toString()
                    if (username.isNotEmpty()) {
                        try {
                            val availability = authRepository.checkUsernameAvailability(username)
                            if (availability) {
                                val registered = authRepository.register(
                                    RegisterRequest(
                                        mobileNumber = mobileNumber,
                                        username = username,
                                        password = password,
                                        displayName = displayName
                                    )
                                )
                                if (registered) _registerState.value =
                                    _registerState.value.copy(isValid = true, registrationPage = 4)
                                else {
                                    _registerState.value =
                                        _registerState.value.copy(isValid = false)
                                }
                            } else _registerState.value = _registerState.value.copy(
                                isValid = false,
                                usernameAvailable = false
                            )
                        } catch (e: Exception) {
                            Log.e("Error Response", e.message.toString())
                        }
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

    fun changeCountryCode(countryCode: String) {
        _registerState.value = _registerState.value.copy(countryCode = countryCode)
        validateForm1()
    }

    fun changeMobileNumber(mobileNumber: String) {
        _registerState.value = _registerState.value.copy(mobileNumber = mobileNumber)
        validateForm1()
    }

    private fun validateForm1() {
        var valid = true
        val mobileNumber = _registerState.value.mobileNumber
        val countryCode = _registerState.value.countryCode
        if (!mobileRegex.matches(
                getFormattedPhoneNumber(
                    countryCode.toString(),
                    mobileNumber.toString()
                )
            )
        )
            valid = false
        _registerState.value = _registerState.value.copy(isValid = valid)
    }

    fun changeOTP(otp: String) {
        _registerState.value = _registerState.value.copy(otp = otp)
        validateForm2()
    }

    private fun validateForm2() {
        var valid = true
        if (_registerState.value.otp.toString().length != 6) valid = false
        _registerState.value = _registerState.value.copy(isValid = valid)
    }

    fun changeUsername(username: String) {
        _registerState.value = _registerState.value.copy(username = username)
        runnable?.let {
            handler?.removeCallbacks(it)
        }

        // Schedule a new runnable after the cooldown period
        runnable = Runnable {
            // Call your API function here
            checkUsernameAvailability()
        }

        handler?.postDelayed(runnable!!, apiCooldownMillis)

        validateForm3()
    }

    fun changePassword(password: String) {
        _registerState.value = _registerState.value.copy(password = password)
        validateForm3()
    }

    fun changeRepeatPassword(repeatPassword: String) {
        _registerState.value = _registerState.value.copy(repeatPassword = repeatPassword)
        validateForm3()
    }

    private fun validateForm3() {
        // Can Optimize this by checking individually and having separate booleans
        val username = _registerState.value.username
        val password = _registerState.value.password
        val repeatPassword = _registerState.value.repeatPassword
        var valid = true
        if (username.toString().length < 4 || username.toString().length > 24) valid = false
        if (!passwordRegex.matches(password.toString())) valid = false
        if (password.toString() != repeatPassword.toString()) valid = false
        _registerState.value = _registerState.value.copy(isValid = valid)
    }

    fun changeDisplayName(displayName: String) {
        _registerState.value = _registerState.value.copy(displayName = displayName)
        validateForm4()
    }

    fun changeProfileUrl(profileUrl: String) {
        _registerState.value = _registerState.value.copy(profileUrl = profileUrl)
        validateForm4()
    }

    private fun validateForm4() {
        var valid = true
        val displayName = _registerState.value.displayName
        if (displayName.toString().isEmpty()) valid = false
        _registerState.value = _registerState.value.copy(isValid = valid)
        // TODO: Validate profileUrl if needed
    }

    fun checkUsernameAvailability() {
        GlobalScope.launch(Dispatchers.IO) {
            _registerState.value = _registerState.value.copy(checkingUsername = true)
            val username = registerState.value.username.toString()
            if (username.isNotEmpty()) {
                try {
                    val availability = authRepository.checkUsernameAvailability(username)
                    _registerState.value =
                        _registerState.value.copy(usernameAvailable = availability)
                } catch (e: Exception) {
                    _registerState.value = _registerState.value.copy(usernameAvailable = false)
                }
            }
            _registerState.value = _registerState.value.copy(checkingUsername = false)
        }
    }

    fun updateProfile(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            _registerState.value = _registerState.value.copy(checkingUsername = true)
            try {
                // TODO: Add profile url if image upload is implemented
                val isUpdated = authRepository.updateUserProfile(
                    UpdateProfileRequest(
                        displayName = _registerState.value.displayName
                    )
                )
                if (!isUpdated) GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Profile Update Failed", Toast.LENGTH_LONG)
                }
            } catch (e: Exception) {
                Log.e("Profile Error", "Error on Updating Profile")
            }
        }
    }
}