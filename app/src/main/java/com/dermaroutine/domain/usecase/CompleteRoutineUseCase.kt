package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.repository.RoutineRepository
import javax.inject.Inject

class CompleteRoutineUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.completeRoutine(id)
    }
}
