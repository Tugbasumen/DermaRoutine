package com.dermaroutine.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class IngredientExplanationRequest(
    val ingredientName: String
)

@Serializable
data class IngredientExplanationResponse(
    val ingredientName: String,
    val summary: String,
    val commonUses: List<String>,
    val considerations: List<String>,
    val disclaimer: String
)

@Serializable
data class ProductAnalysisRequest(
    val ingredients: List<String>
)

@Serializable
data class ProductAnalysisResponse(
    val suitabilityScore: Int,
    val summary: String,
    val keyBenefits: List<String>,
    val potentialRisks: List<String>
)
