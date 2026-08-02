package com.dermaroutine.domain.repository

import com.dermaroutine.domain.model.IngredientExplanation

interface AssistantRepository {
    suspend fun getIngredientExplanation(name: String): Result<IngredientExplanation>
}
