package com.teknophase.chat.di

import com.teknophase.chat.network.RetrofitInstance
import com.teknophase.chat.network.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Named("ApiBaseDomain")
    fun providesBaseDomain(): String {
        return "http://192.168.29.84:3000/"
//        return "http://172.19.101.193:3000/"  // Presidio IP
    }

    @Provides
    fun providesAuthService(@Named("ApiBaseDomain") baseDomain: String): AuthService {
        return RetrofitInstance.build(baseDomain).create(AuthService::class.java)
    }
}