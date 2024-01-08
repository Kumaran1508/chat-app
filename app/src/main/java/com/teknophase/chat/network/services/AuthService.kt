package com.teknophase.chat.network.services

import com.teknophase.chat.data.request.AuthRequest
import com.teknophase.chat.data.request.RegisterRequest
import com.teknophase.chat.data.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("auth/signup")
    suspend fun signup(@Body user: RegisterRequest) : Response<Boolean>

    @GET("user/check-username")
    suspend fun checkUsernameAvailability(@Query("username") username: String) : Response<Boolean>

    @POST("auth/login")
    suspend fun login(@Body user: AuthRequest) : Response<AuthResponse>
}