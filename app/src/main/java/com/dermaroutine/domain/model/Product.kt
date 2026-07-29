package com.dermaroutine.domain.model

data class Product(
    val id: Int,
    val name: String,
    val brand: String,
    val category: ProductCategory,
    val barcode: String?,
    val imageUrl: String?,
    val notes: String?
)

data class ProductDetail(
    val id: Int,
    val name: String,
    val brand: String,
    val category: ProductCategory,
    val barcode: String?,
    val imageUrl: String?,
    val notes: String?,
    val ingredients: List<ProductIngredient>
)

data class ProductIngredient(
    val id: Int,
    val name: String,
    val position: Int,
    val description: String?
)

enum class ProductCategory {
    Cleanser, Toner, Serum, Moisturizer, Sunscreen, Mask, Exfoliant, EyeCare, Treatment, Other
}
