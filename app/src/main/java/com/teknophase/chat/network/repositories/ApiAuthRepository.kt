package com.teknophase.chat.network.repositories

import com.teknophase.chat.data.request.AuthRequest
import com.teknophase.chat.data.request.RegisterRequest
import com.teknophase.chat.data.request.UpdateProfileRequest
import com.teknophase.chat.data.response.AuthResponse
import com.teknophase.chat.network.services.AuthService
import javax.inject.Inject

class ApiAuthRepository @Inject constructor(private val authService: AuthService) : AuthRepository {
    override suspend fun login(user: AuthRequest): AuthResponse {
        val response = authService.login(user)
        if (response.isSuccessful && response.body() != null) return response.body()!!
        else throw Exception(response.message())
    }

    override suspend fun register(user: RegisterRequest): Boolean {
        val response = authService.signup(user)
        if (response.isSuccessful && response.body() != null) return response.body()!!
        else throw Exception(response.message())
    }

    override suspend fun checkUsernameAvailability(username: String): Boolean {
        val response = authService.checkUsernameAvailability(username)
        if (response.isSuccessful && response.body() != null) return response.body()!!
        else throw Exception(response.message())
    }

    override suspend fun updateUserProfile(profileRequest: UpdateProfileRequest): Boolean {
        val response = authService.updateProfile(profileRequest)
        if (response.isSuccessful && response.body() != null) return response.body()!!
        else throw Exception(response.message())
    }
}