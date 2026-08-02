package com.dermaroutine.data.mapper

import com.dermaroutine.data.remote.dto.IngredientExplanationResponse
import com.dermaroutine.domain.model.IngredientExplanation

fun IngredientExplanationResponse.toDomain(): IngredientExplanation {
    return IngredientExplanation(
        ingredientName = ingredientName,
        summary = summary,
        commonUses = commonUses,
        considerations = considerations,
        disclaimer = disclaimer
    )
}
