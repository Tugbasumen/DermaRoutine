package com.dermaroutine.data.remote

import com.dermaroutine.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AssistantApiService {
    @POST("api/assistant/ingredient")
    suspend fun getIngredientExplanation(@Body request: IngredientExplanationRequest): Response<IngredientExplanationResponse>

    @POST("api/assistant/analyze-product")
    suspend fun analyzeProduct(@Body request: ProductAnalysisRequest): Response<ProductAnalysisResponse>

    @POST("api/assistant/chat")
    suspend fun chat(@Body request: GeneralChatRequest): Response<GeneralChatResponse>
}
