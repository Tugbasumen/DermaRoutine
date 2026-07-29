package com.dermaroutine.data.remote

import com.dermaroutine.data.remote.dto.CreateProfileRequest
import com.dermaroutine.data.remote.dto.SkinProfileDto
import com.dermaroutine.data.remote.dto.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.*

interface ProfileApiService {
    @GET("api/profile")
    suspend fun getProfile(): Response<SkinProfileDto>

    @POST("api/profile")
    suspend fun createProfile(@Body request: CreateProfileRequest): Response<SkinProfileDto>

    @PUT("api/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<Unit>
}
