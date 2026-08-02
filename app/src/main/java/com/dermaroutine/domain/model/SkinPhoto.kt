package com.dermaroutine.domain.model

data class SkinPhoto(
    val id: Int,
    val imageUrl: String,
    val takenAt: String, // ISO 8601
    val rating: Int,
    val notes: String?
)
