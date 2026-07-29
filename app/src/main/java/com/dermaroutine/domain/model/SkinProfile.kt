package com.dermaroutine.domain.model

data class SkinProfile(
    val skinType: SkinType,
    val sensitivityLevel: SensitivityLevel,
    val concerns: List<String>,
    val notes: String?
)

enum class SkinType {
    Dry, Oily, Combination, Normal, NotSure
}

enum class SensitivityLevel {
    Low, Medium, High
}
