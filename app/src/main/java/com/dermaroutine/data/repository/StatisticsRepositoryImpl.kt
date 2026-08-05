package com.dermaroutine.data.repository

import com.dermaroutine.data.remote.StatisticsApiService
import com.dermaroutine.data.remote.dto.ProgressSummaryDto
import com.dermaroutine.domain.model.MoodStat
import com.dermaroutine.domain.model.ProgressStats
import com.dermaroutine.domain.repository.StatisticsRepository
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val apiService: StatisticsApiService
) : StatisticsRepository {

    override suspend fun getProgressStats(): Result<ProgressStats> {
        return try {
            val response = apiService.getProgressSummary()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("İstatistikler yüklenemedi."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun ProgressSummaryDto.toDomain(): ProgressStats {
        return ProgressStats(
            morningCompletionRate = morningCompletionRate,
            eveningCompletionRate = eveningCompletionRate,
            currentStreak = currentStreak,
            moodCounts = moodCounts.map { MoodStat(it.mood, it.count) }
        )
    }
}
