package com.teknophase.chat.di

import com.teknophase.chat.network.repositories.ApiAuthRepository
import com.teknophase.chat.network.repositories.AuthRepository
import com.teknophase.chat.network.repositories.MessageRepository
import com.teknophase.chat.network.repositories.SocketMessageRepository
import com.teknophase.chat.network.services.AuthService
import com.teknophase.chat.viewmodel.HomeViewModel
import com.teknophase.chat.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesLoginViewModel(authRepository: AuthRepository): LoginViewModel {
        return LoginViewModel(authRepository)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(authService: AuthService): AuthRepository {
        return ApiAuthRepository(authService)
    }

    @Provides
    @Singleton
    fun providesMessageRepository(): MessageRepository {
        return SocketMessageRepository()
    }

    @Provides
    @Singleton
    fun providesHomeViewModel(messageRepository: MessageRepository): HomeViewModel {
        return HomeViewModel(messageRepository)
    }

}
