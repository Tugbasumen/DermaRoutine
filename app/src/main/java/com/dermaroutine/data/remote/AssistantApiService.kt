package com.dermaroutine.data.remote

import com.dermaroutine.data.remote.dto.IngredientExplanationRequest
import com.dermaroutine.data.remote.dto.IngredientExplanationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AssistantApiService {
    @POST("api/assistant/ingredient")
    suspend fun getIngredientExplanation(@Body request: IngredientExplanationRequest): Response<IngredientExplanationResponse>
}
