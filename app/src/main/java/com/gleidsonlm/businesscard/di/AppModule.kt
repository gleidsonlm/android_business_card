package com.gleidsonlm.businesscard.di

import android.content.Context
import com.gleidsonlm.businesscard.data.repository.ThreatEventRepository
import com.gleidsonlm.businesscard.data.repository.ThreatEventRepositoryImpl
import com.gleidsonlm.businesscard.data.repository.UserRepository
import com.gleidsonlm.businesscard.data.repository.UserRepositoryImpl
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
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideUserRepository(context: Context): UserRepository {
        return UserRepositoryImpl(context)
    }
    
    @Provides
    @Singleton
    fun provideThreatEventRepository(context: Context): ThreatEventRepository {
        return ThreatEventRepositoryImpl(context)
    }
}
