package com.dermaroutine.core.di

import com.dermaroutine.data.remote.AssistantApiService
import com.dermaroutine.data.repository.AssistantRepositoryImpl
import com.dermaroutine.domain.repository.AssistantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AssistantModule {

    @Provides
    @Singleton
    fun provideAssistantApiService(retrofit: Retrofit): AssistantApiService {
        return retrofit.create(AssistantApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAssistantRepository(impl: AssistantRepositoryImpl): AssistantRepository = impl
}
