package com.teknophase.chat.network.repositories

import com.teknophase.chat.data.request.AuthRequest
import com.teknophase.chat.data.request.RegisterRequest
import com.teknophase.chat.data.response.AuthResponse
import com.teknophase.chat.network.services.AuthService
import retrofit2.Response
import javax.inject.Inject

class ApiAuthRepository @Inject constructor(private val authService: AuthService) : AuthRepository {
    override suspend fun login(user: AuthRequest): Response<AuthResponse> {
        return authService.login(user)
    }

    override suspend fun register(user: RegisterRequest): Response<Boolean> {
        return authService.signup(user)
    }

    override suspend fun checkUsernameAvailability(username: String): Response<Boolean> {
        return authService.checkUsernameAvailability(username)
    }
}