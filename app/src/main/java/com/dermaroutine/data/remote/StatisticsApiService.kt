package com.dermaroutine.data.remote

import com.dermaroutine.data.remote.dto.ProgressSummaryDto
import retrofit2.Response
import retrofit2.http.GET

interface StatisticsApiService {
    @GET("api/statistics/summary")
    suspend fun getProgressSummary(): Response<ProgressSummaryDto>
}
