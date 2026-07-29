package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteProduct(id)
    }
}
