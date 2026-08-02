package com.dermaroutine.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SkinPhotoDto(
    val id: Int,
    val imageUrl: String,
    val takenAt: String,
    val rating: Int,
    val notes: String? = null
)

@Serializable
data class CreateSkinPhotoRequest(
    val imageUrl: String,
    val takenAt: String,
    val rating: Int,
    val notes: String? = null
)

@Serializable
data class UpdateSkinPhotoRequest(
    val takenAt: String,
    val rating: Int,
    val notes: String? = null
)
