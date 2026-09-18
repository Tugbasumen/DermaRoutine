package com.dermaroutine.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_items")
data class RoutineItemEntity(
    @PrimaryKey val id: Int,
    val routineId: Int,
    val productId: Int,
    val productName: String,
    val productBrand: String?,
    val productImageUrl: String?,
    val stepOrder: Int,
    val notes: String?,
    val isCompletedToday: Boolean
)
