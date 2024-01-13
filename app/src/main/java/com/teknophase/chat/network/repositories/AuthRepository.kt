package com.teknophase.chat.network.repositories

import com.teknophase.chat.data.request.AuthRequest
import com.teknophase.chat.data.request.RegisterRequest
import com.teknophase.chat.data.request.UpdateProfileRequest
import com.teknophase.chat.data.response.AuthResponse

interface AuthRepository {
    suspend fun login(user: AuthRequest): AuthResponse

    suspend fun register(user: RegisterRequest): Boolean

    suspend fun checkUsernameAvailability(username: String): Boolean

    suspend fun updateUserProfile(profileRequest: UpdateProfileRequest): Boolean
}