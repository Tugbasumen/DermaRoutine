package com.dermaroutine.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SkinProfileDto(
    val skinType: Int,
    val sensitivityLevel: Int,
    val concerns: List<String>,
    val notes: String?
)

@Serializable
data class CreateProfileRequest(
    val skinType: Int,
    val sensitivityLevel: Int,
    val concerns: List<String>,
    val notes: String?
)

@Serializable
data class UpdateProfileRequest(
    val skinType: Int,
    val sensitivityLevel: Int,
    val concerns: List<String>,
    val notes: String?
)
