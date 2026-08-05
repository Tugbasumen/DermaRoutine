package com.dermaroutine.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RoutineDto(
    val id: Int,
    val name: String,
    val routineType: Int,
    val isActive: Boolean,
    val itemCount: Int = 0,
    val completedItemCount: Int = 0,
    val isCompletedToday: Boolean = false
)

@Serializable
data class RoutineDetailDto(
    val id: Int,
    val name: String,
    val routineType: Int,
    val isActive: Boolean,
    val items: List<RoutineItemDto>,
    val isCompletedToday: Boolean
)

@Serializable
data class RoutineItemDto(
    val id: Int,
    val productId: Int,
    val productName: String,
    val productBrand: String? = null,
    val productImageUrl: String? = null,
    val stepOrder: Int,
    val notes: String? = null,
    val isCompletedToday: Boolean = false
)

@Serializable
data class CreateRoutineRequest(
    val name: String,
    val routineType: Int
)

@Serializable
data class StreakDto(
    val currentStreak: Int,
    val todayCompleted: Boolean
)

@Serializable
data class AddRoutineItemRequest(
    val productId: Int,
    val stepOrder: Int,
    val notes: String? = null
)
