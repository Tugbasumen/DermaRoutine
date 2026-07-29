package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.ProductDetail
import com.dermaroutine.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Result<ProductDetail> {
        return repository.getProduct(id)
    }
}
