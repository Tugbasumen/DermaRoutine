package com.dermaroutine.domain.repository

import com.dermaroutine.domain.model.*

interface RoutineRepository {
    suspend fun getRoutines(): Result<List<Routine>>
    suspend fun getRoutine(id: Int): Result<RoutineDetail>
    suspend fun createRoutine(name: String, routineType: RoutineType): Result<Routine>
    suspend fun completeRoutine(id: Int): Result<Unit>
    suspend fun getStreak(): Result<StreakInfo>
    suspend fun addItemToRoutine(routineId: Int, productId: Int, stepOrder: Int, notes: String?): Result<Unit>
    suspend fun removeItemFromRoutine(routineId: Int, itemId: Int): Result<Unit>
    suspend fun toggleItemCompletion(itemId: Int): Result<Unit>
}
