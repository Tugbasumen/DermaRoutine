package com.dermaroutine.domain.model

data class ProductAnalysis(
    val suitabilityScore: Int,
    val summary: String,
    val keyBenefits: List<String>,
    val potentialRisks: List<String>
)
