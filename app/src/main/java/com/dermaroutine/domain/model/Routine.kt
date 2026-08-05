package com.dermaroutine.domain.model

data class Routine(
    val id: Int,
    val name: String,
    val routineType: RoutineType,
    val isActive: Boolean,
    val itemCount: Int,
    val completedItemCount: Int,
    val isCompletedToday: Boolean
)

data class RoutineDetail(
    val id: Int,
    val name: String,
    val routineType: RoutineType,
    val isActive: Boolean,
    val items: List<RoutineItem>,
    val isCompletedToday: Boolean
)

data class RoutineItem(
    val id: Int,
    val productId: Int,
    val productName: String,
    val productBrand: String?,
    val productImageUrl: String?,
    val stepOrder: Int,
    val notes: String?,
    val isCompletedToday: Boolean
)

enum class RoutineType {
    Morning, Evening
}

data class StreakInfo(
    val currentStreak: Int,
    val todayCompleted: Boolean
)
