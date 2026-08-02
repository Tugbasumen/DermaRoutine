package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.IngredientExplanation
import com.dermaroutine.domain.repository.AssistantRepository
import javax.inject.Inject

class GetIngredientExplanationUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(name: String): Result<IngredientExplanation> {
        return repository.getIngredientExplanation(name)
    }
}
