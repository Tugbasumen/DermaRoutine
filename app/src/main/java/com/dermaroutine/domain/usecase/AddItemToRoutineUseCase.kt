package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.repository.RoutineRepository
import javax.inject.Inject

class AddItemToRoutineUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(routineId: Int, productId: Int, stepOrder: Int, notes: String?): Result<Unit> {
        return repository.addItemToRoutine(routineId, productId, stepOrder, notes)
    }
}
