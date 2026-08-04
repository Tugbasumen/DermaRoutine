package com.dermaroutine.presentation.home

data class HomeUiState(
    val greetingRes: Int = 0,
    val userName: String = "",
    val date: String = "",
    val morningRoutineStatus: String = "0/0 completed",
    val eveningRoutineStatus: String = "0/0 completed",
    val currentStreak: Int = 0,
    val hasTodayPhoto: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
