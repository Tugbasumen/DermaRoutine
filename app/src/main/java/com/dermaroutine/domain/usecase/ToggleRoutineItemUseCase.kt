package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.repository.RoutineRepository
import javax.inject.Inject

class ToggleRoutineItemUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(itemId: Int): Result<Unit> {
        return repository.toggleItemCompletion(itemId)
    }
}
