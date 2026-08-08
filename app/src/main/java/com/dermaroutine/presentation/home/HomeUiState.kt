package com.dermaroutine.presentation.home

import com.dermaroutine.domain.model.RoutineDetail

data class HomeUiState(
    val greetingRes: Int = 0,
    val userName: String = "",
    val date: String = "",
    val morningRoutine: RoutineDetail? = null,
    val eveningRoutine: RoutineDetail? = null,
    val currentStreak: Int = 0,
    val skinScore: Int = 0,
    val weeklyActivity: List<Int> = emptyList(),
    val nextReminderTitleRes: Int? = null,
    val nextReminderTime: String? = null,
    val hasTodayPhoto: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
