package com.teknophase.chat.network.repositories.mock

import com.teknophase.chat.data.request.AuthRequest
import com.teknophase.chat.data.request.RegisterRequest
import com.teknophase.chat.data.request.UpdateProfileRequest
import com.teknophase.chat.data.response.AuthResponse
import com.teknophase.chat.data.response.UserResponse
import com.teknophase.chat.network.repositories.interfaces.AuthRepository

class MockAuthRepository : AuthRepository {
    override suspend fun login(user: AuthRequest): AuthResponse {
        return AuthResponse("","")
    }

    override suspend fun register(user: RegisterRequest): Boolean {
        return true
    }

    override suspend fun checkUsernameAvailability(username: String): Boolean {
        return true
    }

    override suspend fun updateUserProfile(profileRequest: UpdateProfileRequest): Boolean {
        return true
    }

    override suspend fun getUserProfile(username: String): UserResponse {
        return UserResponse(
            id = "",
            mobileNumber = "",
            username = "",
            displayName = null,
            profileUrl = null,
            about = null,
            isOnline = null,
            lastOnline = null
        )
    }

    override suspend fun getUserProfileById(id: String): UserResponse {
        return UserResponse(
            id = "",
            mobileNumber = "",
            username = "",
            displayName = null,
            profileUrl = null,
            about = null,
            isOnline = null,
            lastOnline = null
        )
    }
}