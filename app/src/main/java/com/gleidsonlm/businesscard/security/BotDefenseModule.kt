package com.gleidsonlm.businesscard.security

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing bot defense related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object BotDefenseModule {

    @Provides
    @Singleton
    fun provideBotDefenseConfig(): BotDefenseConfig {
        return BotDefenseConfig(
            sensitivityLevel = BotSensitivityLevel.MEDIUM,
            enableUserNotification = true,
            enableSecurityCountermeasures = true,
            enableAppProtection = false, // Can be enabled for production
            isLogOnlyMode = false // Can be set to true for development/testing
        )
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}