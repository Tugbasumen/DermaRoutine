package com.dermaroutine.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val name: String,
    val brand: String,
    val category: Int,
    val barcode: String? = null,
    val imageUrl: String? = null,
    val notes: String? = null
)

@Serializable
data class ProductDetailDto(
    val id: Int,
    val name: String,
    val brand: String,
    val category: Int,
    val barcode: String? = null,
    val imageUrl: String? = null,
    val notes: String? = null,
    val ingredients: List<ProductIngredientDto>
)

@Serializable
data class ProductIngredientDto(
    val id: Int,
    val name: String,
    val position: Int,
    val description: String? = null
)

@Serializable
data class CreateProductRequest(
    val name: String,
    val brand: String,
    val category: Int,
    val barcode: String? = null,
    val notes: String? = null
)

@Serializable
data class AddIngredientRequest(
    val name: String,
    val position: Int,
    val description: String? = null
)
