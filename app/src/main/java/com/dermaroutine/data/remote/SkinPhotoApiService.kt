package com.dermaroutine.data.remote

import com.dermaroutine.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface SkinPhotoApiService {
    @GET("api/skinphoto")
    suspend fun getPhotos(): Response<List<SkinPhotoDto>>

    @GET("api/skinphoto/{id}")
    suspend fun getPhoto(@Path("id") id: Int): Response<SkinPhotoDto>

    @POST("api/skinphoto")
    suspend fun createPhoto(@Body request: CreateSkinPhotoRequest): Response<SkinPhotoDto>

    @PUT("api/skinphoto/{id}")
    suspend fun updatePhoto(@Path("id") id: Int, @Body request: UpdateSkinPhotoRequest): Response<Unit>

    @DELETE("api/skinphoto/{id}")
    suspend fun deletePhoto(@Path("id") id: Int): Response<Unit>
}
