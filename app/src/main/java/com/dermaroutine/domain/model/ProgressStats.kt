package com.dermaroutine.domain.model

data class ProgressStats(
    val morningCompletionRate: Double,
    val eveningCompletionRate: Double,
    val currentStreak: Int,
    val moodCounts: List<MoodStat>
)

data class MoodStat(
    val mood: String,
    val count: Int
)
