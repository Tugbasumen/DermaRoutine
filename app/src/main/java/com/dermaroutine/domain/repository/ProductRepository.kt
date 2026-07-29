package com.dermaroutine.domain.repository

import com.dermaroutine.domain.model.*

interface ProductRepository {
    suspend fun getProducts(search: String? = null, category: ProductCategory? = null): Result<List<Product>>
    suspend fun getProduct(id: Int): Result<ProductDetail>
    suspend fun createProduct(product: Product): Result<Product>
    suspend fun updateProduct(id: Int, product: Product): Result<Unit>
    suspend fun deleteProduct(id: Int): Result<Unit>
    suspend fun addIngredient(productId: Int, ingredient: ProductIngredient): Result<Unit>
    suspend fun saveIngredientsBulk(productId: Int, ingredients: List<ProductIngredient>): Result<Unit>
    suspend fun getProductByBarcode(barcode: String): Result<Product?>
}
