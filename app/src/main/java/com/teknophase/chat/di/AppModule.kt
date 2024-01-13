package com.teknophase.chat.di

import com.teknophase.chat.network.repositories.ApiAuthRepository
import com.teknophase.chat.network.repositories.AuthRepository
import com.teknophase.chat.network.services.AuthService
import com.teknophase.chat.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesLoginViewModel(): LoginViewModel {
        return LoginViewModel()
    }

    @Provides
    fun providesAuthRepository(authService: AuthService): AuthRepository {
        return ApiAuthRepository(authService)
    }

}
