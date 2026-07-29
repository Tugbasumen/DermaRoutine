package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.Routine
import com.dermaroutine.domain.model.RoutineType
import com.dermaroutine.domain.repository.RoutineRepository
import javax.inject.Inject

class CreateRoutineUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(name: String, type: RoutineType): Result<Routine> {
        return repository.createRoutine(name, type)
    }
}
