package com.teknophase.chat.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.teknophase.chat.data.model.DestinationTypeDeserializer
import com.teknophase.chat.data.model.MessageDestinationType
import com.teknophase.chat.data.model.MessageStatus
import com.teknophase.chat.data.model.MessageStatusDeserializer
import com.teknophase.chat.data.model.MessageType
import com.teknophase.chat.data.model.MessageTypeDeserializer
import com.teknophase.chat.network.repositories.ApiAuthRepository
import com.teknophase.chat.network.repositories.AuthRepository
import com.teknophase.chat.network.repositories.MessageRepository
import com.teknophase.chat.network.repositories.SocketMessageRepository
import com.teknophase.chat.network.services.AuthService
import com.teknophase.chat.viewmodel.ChatViewModel
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
    fun providesHomeViewModel(
        messageRepository: MessageRepository,
        gson: Gson,
        authRepository: AuthRepository
    ): HomeViewModel {
        return HomeViewModel(
            messageRepository = messageRepository,
            gson = gson,
            authRepository = authRepository
        )
    }

    @Provides
    @Singleton
    fun providesChatViewModel(messageRepository: MessageRepository): ChatViewModel {
        return ChatViewModel(messageRepository)
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                MessageDestinationType::class.java,
                DestinationTypeDeserializer()
            )
            .registerTypeAdapter(
                MessageType::class.java,
                MessageTypeDeserializer()
            )
            .registerTypeAdapter(
                MessageStatus::class.java,
                MessageStatusDeserializer()
            )
            .create()
    }

}
