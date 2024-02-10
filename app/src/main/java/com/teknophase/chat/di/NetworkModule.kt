package com.teknophase.chat.di

import com.teknophase.chat.network.RetrofitInstance
import com.teknophase.chat.network.services.AppConfigService
import com.teknophase.chat.network.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

const val API_BASE_DOMAIN_KEY = "ApiBaseDomain"
const val WEBSOCKET_BASE_DOMAIN_KEY = "WebSocketBaseDomain"

const val WEB_SOCKET_URL = "ws://192.168.29.84:3000/"
const val WEB_API_URL = "http://192.168.29.84:3000/"
const val WEB_SOCKET_URL_WORK = "ws://172.19.101.213:3000/"
const val WEB_API_URL_WORK = "http://172.19.101.213:3000/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Named(API_BASE_DOMAIN_KEY)
    fun providesBaseDomain(): String {
//        return WEB_API_URL_WORK
        return WEB_API_URL
    }

    @Provides
    @Named(WEBSOCKET_BASE_DOMAIN_KEY)
    fun providesWebSocketBaseDomain(): String {
//        return WEB_SOCKET_URL_WORK
        return WEB_SOCKET_URL
    }

    @Provides
    fun providesAuthService(@Named(API_BASE_DOMAIN_KEY) baseDomain: String): AuthService {
        return RetrofitInstance.build(baseDomain).create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun providesAppConfigService(@Named(API_BASE_DOMAIN_KEY) baseDomain: String): AppConfigService {
        return RetrofitInstance.build(baseDomain).create(AppConfigService::class.java)
    }
}