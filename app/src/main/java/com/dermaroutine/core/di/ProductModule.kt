package com.dermaroutine.core.di

import com.dermaroutine.data.remote.ProductApiService
import com.dermaroutine.data.repository.ProductRepositoryImpl
import com.dermaroutine.data.scanner.MlKitBarcodeScanner
import com.dermaroutine.domain.repository.ProductRepository
import com.dermaroutine.domain.scanner.BarcodeScanner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductRepository(impl: ProductRepositoryImpl): ProductRepository = impl

    @Provides
    @Singleton
    fun provideBarcodeScanner(impl: MlKitBarcodeScanner): BarcodeScanner = impl
}
