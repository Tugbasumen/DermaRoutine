package com.dermaroutine.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dermaroutine.domain.model.RoutineType

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val routineType: RoutineType,
    val isActive: Boolean,
    val itemCount: Int,
    val completedItemCount: Int,
    val isCompletedToday: Boolean
)
