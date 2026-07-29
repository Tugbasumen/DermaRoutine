package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.StreakInfo
import com.dermaroutine.domain.repository.RoutineRepository
import javax.inject.Inject

class GetStreakUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(): Result<StreakInfo> {
        return repository.getStreak()
    }
}
