package com.dermaroutine.data.remote

import com.dermaroutine.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface RoutineApiService {
    @GET("api/routine")
    suspend fun getRoutines(): Response<List<RoutineDto>>

    @GET("api/routine/{id}")
    suspend fun getRoutine(@Path("id") id: Int): Response<RoutineDetailDto>

    @POST("api/routine")
    suspend fun createRoutine(@Body request: CreateRoutineRequest): Response<RoutineDto>

    @POST("api/routine/{id}/complete")
    suspend fun completeRoutine(@Path("id") id: Int): Response<Unit>

    @GET("api/routine/streak")
    suspend fun getStreak(): Response<StreakDto>

    @POST("api/routine/{id}/items")
    suspend fun addItemToRoutine(@Path("id") routineId: Int, @Body request: AddRoutineItemRequest): Response<Unit>

    @DELETE("api/routine/{id}/items/{itemId}")
    suspend fun removeItemFromRoutine(@Path("id") routineId: Int, @Path("itemId") itemId: Int): Response<Unit>
}
