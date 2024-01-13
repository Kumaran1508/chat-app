package com.teknophase.chat.di

import com.teknophase.chat.network.RetrofitInstance
import com.teknophase.chat.network.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Named("ApiBaseDomain")
    fun providesBaseDomain() : String {
        return "http://192.168.29.84:3000/"
    }

    @Provides
    fun providesAuthService(@Named("ApiBaseDomain") baseDomain: String) : AuthService {
        return RetrofitInstance.build(baseDomain).create(AuthService::class.java)
    }
}