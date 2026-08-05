package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.ProgressStats
import com.dermaroutine.domain.repository.StatisticsRepository
import javax.inject.Inject

class GetProgressStatsUseCase @Inject constructor(
    private val repository: StatisticsRepository
) {
    suspend operator fun invoke(): Result<ProgressStats> {
        return repository.getProgressStats()
    }
}
