package com.teknophase.chat.di

import com.teknophase.chat.network.RetrofitInstance
import com.teknophase.chat.network.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

const val API_BASE_DOMAIN_KEY = "ApiBaseDomain"
const val WEBSOCKET_BASE_DOMAIN_KEY = "WebSocketBaseDomain"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Named(API_BASE_DOMAIN_KEY)
    fun providesBaseDomain(): String {
        return ""
    }

    @Provides
    @Named(WEBSOCKET_BASE_DOMAIN_KEY)
    fun providesWebSocketBaseDomain(): String {
        return ""
    }

    @Provides
    fun providesAuthService(@Named(API_BASE_DOMAIN_KEY) baseDomain: String): AuthService {
        return RetrofitInstance.build(baseDomain).create(AuthService::class.java)
    }
}