package com.dermaroutine.domain.scanner

class IngredientTextParser {
    fun parse(text: String): List<String> {
        if (text.isBlank()) return emptyList()

        // Clean up common ingredient list noise
        val cleanedText = text
            .replace(Regex("(?i)ingredients:?"), "")
            .replace(Regex("(?i)contains:?"), "")
            .replace("\n", ", ")

        return cleanedText
            .split(Regex("[,;\\.]"))
            .map { it.trim() }
            .filter { it.isNotBlank() && it.length > 1 }
            .distinctBy { it.lowercase() }
    }
}
