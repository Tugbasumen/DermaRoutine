package com.dermaroutine.data.repository

import com.dermaroutine.data.mapper.toDomain
import com.dermaroutine.data.remote.AssistantApiService
import com.dermaroutine.data.remote.dto.IngredientExplanationRequest
import com.dermaroutine.domain.model.IngredientExplanation
import com.dermaroutine.domain.repository.AssistantRepository
import javax.inject.Inject

class AssistantRepositoryImpl @Inject constructor(
    private val apiService: AssistantApiService
) : AssistantRepository {
    override suspend fun getIngredientExplanation(name: String): Result<IngredientExplanation> {
        return try {
            val response = apiService.getIngredientExplanation(IngredientExplanationRequest(name))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to load AI explanation"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
