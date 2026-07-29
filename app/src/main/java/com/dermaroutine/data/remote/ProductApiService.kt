package com.dermaroutine.data.remote

import com.dermaroutine.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {
    @GET("api/product")
    suspend fun getProducts(
        @Query("search") search: String? = null,
        @Query("category") category: String? = null
    ): Response<List<ProductDto>>

    @GET("api/product/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<ProductDetailDto>

    @POST("api/product")
    suspend fun createProduct(@Body request: CreateProductRequest): Response<ProductDto>

    @PUT("api/product/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body request: CreateProductRequest): Response<Unit>

    @DELETE("api/product/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>

    @POST("api/product/{id}/ingredients")
    suspend fun addIngredient(@Path("id") id: Int, @Body request: AddIngredientRequest): Response<Unit>
}
