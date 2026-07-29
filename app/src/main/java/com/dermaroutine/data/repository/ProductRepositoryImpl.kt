package com.dermaroutine.data.repository

import com.dermaroutine.data.mapper.toDomain
import com.dermaroutine.data.remote.ProductApiService
import com.dermaroutine.data.remote.dto.AddIngredientRequest
import com.dermaroutine.data.remote.dto.BulkAddIngredientsRequest
import com.dermaroutine.data.remote.dto.CreateProductRequest
import com.dermaroutine.domain.model.*
import com.dermaroutine.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService
) : ProductRepository {

    override suspend fun getProducts(search: String?, category: ProductCategory?): Result<List<Product>> {
        return try {
            val response = apiService.getProducts(search, category?.name)
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load products"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProduct(id: Int): Result<ProductDetail> {
        return try {
            val response = apiService.getProduct(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to load product detail"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createProduct(product: Product): Result<Product> {
        return try {
            val request = CreateProductRequest(
                name = product.name,
                brand = product.brand,
                category = product.category.ordinal,
                barcode = product.barcode,
                notes = product.notes
            )
            val response = apiService.createProduct(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to create product"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProduct(id: Int, product: Product): Result<Unit> {
        return try {
            val request = CreateProductRequest(
                name = product.name,
                brand = product.brand,
                category = product.category.ordinal,
                barcode = product.barcode,
                notes = product.notes
            )
            val response = apiService.updateProduct(id, request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Failed to update product"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProduct(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteProduct(id)
            if (response.isSuccessful) Result.success(Unit)
            else if (response.code() == 409) Result.failure(Exception("This product is used in a routine. Remove it from your routines first."))
            else Result.failure(Exception("Failed to delete product"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addIngredient(productId: Int, ingredient: ProductIngredient): Result<Unit> {
        return try {
            val request = AddIngredientRequest(
                name = ingredient.name,
                position = ingredient.position,
                description = ingredient.description
            )
            val response = apiService.addIngredient(productId, request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Failed to add ingredient"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveIngredientsBulk(productId: Int, ingredients: List<ProductIngredient>): Result<Unit> {
        return try {
            val request = BulkAddIngredientsRequest(
                ingredients = ingredients.map { 
                    AddIngredientRequest(it.name, it.position, it.description) 
                }
            )
            val response = apiService.bulkAddIngredients(productId, request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Failed to save ingredients"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductByBarcode(barcode: String): Result<Product?> {
        return try {
            val response = apiService.getProductByBarcode(barcode)
            if (response.isSuccessful) {
                Result.success(response.body()?.toDomain())
            } else if (response.code() == 404) {
                Result.success(null)
            } else {
                Result.failure(Exception("Failed to check barcode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
