package com.dermaroutine.core.di

import com.dermaroutine.data.remote.SkinPhotoApiService
import com.dermaroutine.data.repository.SkinPhotoRepositoryImpl
import com.dermaroutine.domain.repository.SkinPhotoRepository
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SkinPhotoModule {

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideSkinPhotoApiService(retrofit: Retrofit): SkinPhotoApiService {
        return retrofit.create(SkinPhotoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSkinPhotoRepository(impl: SkinPhotoRepositoryImpl): SkinPhotoRepository = impl
}
