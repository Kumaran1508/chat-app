package com.teknophase.chat.network.repositories

import com.teknophase.chat.network.services.AppConfigService
import javax.inject.Inject

class AppConfigRepository @Inject constructor(private val appConfigService: AppConfigService) {
    suspend fun getMinAppVersion(): Long? {
        val response = appConfigService.getMinAppVersion()
        return if (response.isSuccessful && response.body() != null) response.body()
        else null
    }

    suspend fun getLatestAppVersion(): Long? {
        val response = appConfigService.getLatestAppVersion()
        return if (response.isSuccessful && response.body() != null) response.body()
        else null
    }
}