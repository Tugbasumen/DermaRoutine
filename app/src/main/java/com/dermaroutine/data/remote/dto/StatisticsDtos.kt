package com.dermaroutine.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProgressSummaryDto(
    val morningCompletionRate: Double,
    val eveningCompletionRate: Double,
    val currentStreak: Int,
    val skinScore: Int,
    val weeklyActivity: List<Int>,
    val moodCounts: List<MoodStatDto>
)

@Serializable
data class MoodStatDto(
    val mood: String,
    val count: Int
)
