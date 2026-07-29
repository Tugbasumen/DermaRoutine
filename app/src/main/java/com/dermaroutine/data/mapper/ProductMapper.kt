package com.dermaroutine.data.mapper

import com.dermaroutine.data.remote.dto.*
import com.dermaroutine.domain.model.*

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        brand = brand,
        category = ProductCategory.entries[category],
        barcode = barcode,
        imageUrl = imageUrl,
        notes = notes
    )
}

fun ProductDetailDto.toDomain(): ProductDetail {
    return ProductDetail(
        id = id,
        name = name,
        brand = brand,
        category = ProductCategory.entries[category],
        barcode = barcode,
        imageUrl = imageUrl,
        notes = notes,
        ingredients = ingredients.map { it.toDomain() }
    )
}

fun ProductIngredientDto.toDomain(): ProductIngredient {
    return ProductIngredient(
        id = id,
        name = name,
        position = position,
        description = description
    )
}
