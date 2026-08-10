package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.ProductAnalysis
import com.dermaroutine.domain.repository.AssistantRepository
import javax.inject.Inject

class AnalyzeProductUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(ingredients: List<String>): Result<ProductAnalysis> {
        return repository.analyzeProduct(ingredients)
    }
}
