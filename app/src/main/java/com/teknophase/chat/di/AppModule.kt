package com.teknophase.chat.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.teknophase.chat.config.AppConfig
import com.teknophase.chat.data.model.DestinationTypeDeserializer
import com.teknophase.chat.data.model.MessageDestinationType
import com.teknophase.chat.data.model.MessageStatus
import com.teknophase.chat.data.model.MessageStatusDeserializer
import com.teknophase.chat.data.model.MessageType
import com.teknophase.chat.data.model.MessageTypeDeserializer
import com.teknophase.chat.network.repositories.api.ApiAuthRepository
import com.teknophase.chat.network.repositories.api.AppConfigRepository
import com.teknophase.chat.network.repositories.interfaces.AuthRepository
import com.teknophase.chat.network.repositories.interfaces.MessageRepository
import com.teknophase.chat.network.repositories.api.SocketMessageRepository
import com.teknophase.chat.network.services.AppConfigService
import com.teknophase.chat.network.services.AuthService
import com.teknophase.chat.util.NotificationHelper
import com.teknophase.chat.viewmodel.BootstrapViewModel
import com.teknophase.chat.viewmodel.HomeViewModel
import com.teknophase.chat.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        authRepository: AuthRepository,
        @ApplicationContext context: Context,
        notificationHelper: NotificationHelper
    ): HomeViewModel {
        return HomeViewModel(
            messageRepository = messageRepository,
            gson = gson,
            authRepository = authRepository,
            context = context,
            notificationHelper = notificationHelper
        )
    }

    @Provides
    @Singleton
    fun providesNotificationHelper() : NotificationHelper {
        return NotificationHelper()
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

    @Provides
    @Singleton
    fun providesAppConfigRepository(appConfigService: AppConfigService): AppConfigRepository {
        return AppConfigRepository(appConfigService = appConfigService)
    }

    @Provides
    @Singleton
    fun providesAppConfig(appConfigRepository: AppConfigRepository): AppConfig {
        return AppConfig(appConfigRepository = appConfigRepository)
    }

    @Provides
    @Singleton
    fun providesBootstrapViewModel(appConfig: AppConfig): BootstrapViewModel {
        return BootstrapViewModel(appConfig = appConfig)
    }
}
