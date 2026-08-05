package com.dermaroutine.core.di

import com.dermaroutine.data.remote.JournalApiService
import com.dermaroutine.data.repository.JournalRepositoryImpl
import com.dermaroutine.domain.repository.JournalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JournalModule {

    @Provides
    @Singleton
    fun provideJournalApiService(retrofit: Retrofit): JournalApiService {
        return retrofit.create(JournalApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideJournalRepository(apiService: JournalApiService): JournalRepository {
        return JournalRepositoryImpl(apiService)
    }
}
