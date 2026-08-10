package com.dermaroutine.domain.repository

import com.dermaroutine.domain.model.IngredientExplanation
import com.dermaroutine.domain.model.ProductAnalysis

interface AssistantRepository {
    suspend fun getIngredientExplanation(name: String): Result<IngredientExplanation>
    suspend fun analyzeProduct(ingredients: List<String>): Result<ProductAnalysis>
}
