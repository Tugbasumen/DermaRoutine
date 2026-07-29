package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.Product
import com.dermaroutine.domain.repository.ProductRepository
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Result<Product> {
        if (product.name.isBlank()) return Result.failure(Exception("Product name is required"))
        return repository.createProduct(product)
    }
}
