package com.dermaroutine.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dermaroutine.domain.model.ProductCategory

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val brand: String,
    val category: ProductCategory,
    val barcode: String?,
    val imageUrl: String?,
    val notes: String?
)
