package com.dermaroutine.data.repository

import com.dermaroutine.data.mapper.toDomain
import com.dermaroutine.data.remote.AssistantApiService
import com.dermaroutine.data.remote.dto.IngredientExplanationRequest
import com.dermaroutine.data.remote.dto.ProductAnalysisRequest
import com.dermaroutine.domain.model.IngredientExplanation
import com.dermaroutine.domain.model.ProductAnalysis
import com.dermaroutine.domain.repository.AssistantRepository
import javax.inject.Inject

class AssistantRepositoryImpl @Inject constructor(
    private val apiService: AssistantApiService
) : AssistantRepository {

    override suspend fun getIngredientExplanation(name: String): Result<IngredientExplanation> {
        return try {
            val request = IngredientExplanationRequest(name)
            val response = apiService.getIngredientExplanation(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Could not load AI information."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun analyzeProduct(ingredients: List<String>): Result<ProductAnalysis> {
        return try {
            val request = ProductAnalysisRequest(ingredients)
            val response = apiService.analyzeProduct(request)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(ProductAnalysis(
                    suitabilityScore = body.suitabilityScore,
                    summary = body.summary,
                    keyBenefits = body.keyBenefits,
                    potentialRisks = body.potentialRisks
                ))
            } else {
                Result.failure(Exception("Analiz gerçekleştirilemedi."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
