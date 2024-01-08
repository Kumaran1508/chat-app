package com.teknophase.chat.network.repositories

import com.teknophase.chat.data.request.AuthRequest
import com.teknophase.chat.data.request.RegisterRequest
import com.teknophase.chat.data.response.AuthResponse
import com.teknophase.chat.data.response.UserResponse
import retrofit2.Response

interface AuthRepository {
    suspend fun login(user: AuthRequest): Response<AuthResponse>

    suspend fun register(user: RegisterRequest): Response<Boolean>

    suspend fun checkUsernameAvailability(username: String) : Response<Boolean>
}