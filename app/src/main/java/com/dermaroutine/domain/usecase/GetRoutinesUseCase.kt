package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.Routine
import com.dermaroutine.domain.repository.RoutineRepository
import javax.inject.Inject

class GetRoutinesUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(): Result<List<Routine>> {
        return repository.getRoutines()
    }
}
