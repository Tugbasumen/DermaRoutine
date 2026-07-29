package com.dermaroutine.core.di

import com.dermaroutine.data.remote.RoutineApiService
import com.dermaroutine.data.repository.RoutineRepositoryImpl
import com.dermaroutine.domain.repository.RoutineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoutineModule {

    @Provides
    @Singleton
    fun provideRoutineApiService(retrofit: Retrofit): RoutineApiService {
        return retrofit.create(RoutineApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoutineRepository(impl: RoutineRepositoryImpl): RoutineRepository = impl
}
