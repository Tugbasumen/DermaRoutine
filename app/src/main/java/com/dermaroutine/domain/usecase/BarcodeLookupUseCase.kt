package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.Product
import com.dermaroutine.domain.repository.ProductRepository
import javax.inject.Inject

class BarcodeLookupUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(barcode: String): Result<Product?> {
        return repository.getProductByBarcode(barcode)
    }
}
