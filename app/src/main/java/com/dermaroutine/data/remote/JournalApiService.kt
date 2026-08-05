package com.dermaroutine.data.remote

import com.dermaroutine.data.remote.dto.CreateJournalEntryRequest
import com.dermaroutine.data.remote.dto.JournalEntryResponse
import retrofit2.Response
import retrofit2.http.*

interface JournalApiService {
    @GET("api/journal")
    suspend fun getEntries(): Response<List<JournalEntryResponse>>

    @POST("api/journal")
    suspend fun createEntry(@Body request: CreateJournalEntryRequest): Response<JournalEntryResponse>

    @DELETE("api/journal/{id}")
    suspend fun deleteEntry(@Path("id") id: Int): Response<Unit>
}
