package com.teknophase.chat.network.services

import retrofit2.Response
import retrofit2.http.GET

interface AppConfigService {
    @GET("config/min-android-app-version")
    suspend fun getMinAppVersion(): Response<Long>

    @GET("config/latest-android-app-version")
    suspend fun getLatestAppVersion(): Response<Long>
}