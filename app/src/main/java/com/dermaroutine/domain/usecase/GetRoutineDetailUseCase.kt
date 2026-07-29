package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.RoutineDetail
import com.dermaroutine.domain.repository.RoutineRepository
import javax.inject.Inject

class GetRoutineDetailUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(id: Int): Result<RoutineDetail> {
        return repository.getRoutine(id)
    }
}
