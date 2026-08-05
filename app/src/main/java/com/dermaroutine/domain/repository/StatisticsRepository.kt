package com.dermaroutine.domain.repository

import com.dermaroutine.domain.model.ProgressStats

interface StatisticsRepository {
    suspend fun getProgressStats(): Result<ProgressStats>
}
