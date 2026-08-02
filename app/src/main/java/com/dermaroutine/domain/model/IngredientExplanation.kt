package com.dermaroutine.domain.model

data class IngredientExplanation(
    val ingredientName: String,
    val summary: String,
    val commonUses: List<String>,
    val considerations: List<String>,
    val disclaimer: String
)
