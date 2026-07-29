package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.*
import com.dermaroutine.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(search: String? = null, category: ProductCategory? = null): Result<List<Product>> {
        return repository.getProducts(search, category)
    }
}
