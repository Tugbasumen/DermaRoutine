package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.ProductIngredient
import com.dermaroutine.domain.repository.ProductRepository
import javax.inject.Inject

class SaveIngredientsBulkUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int, ingredients: List<ProductIngredient>): Result<Unit> {
        return repository.saveIngredientsBulk(productId, ingredients)
    }
}
