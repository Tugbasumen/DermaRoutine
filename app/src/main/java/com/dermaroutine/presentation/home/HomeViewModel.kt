package com.dermaroutine.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.R
import com.dermaroutine.domain.model.RoutineType
import com.dermaroutine.domain.usecase.GetCurrentUserUseCase
import com.dermaroutine.domain.usecase.GetRoutinesUseCase
import com.dermaroutine.domain.usecase.GetStreakUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getRoutinesUseCase: GetRoutinesUseCase,
    private val getStreakUseCase: GetStreakUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateTimeAndGreeting()
        observeUser()
        loadDashboardData()
    }

    private fun observeUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collectLatest { user ->
                _uiState.value = _uiState.value.copy(
                    userName = user?.displayName ?: user?.email?.split("@")?.get(0) ?: ""
                )
            }
        }
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Parallel execution would be better but keeping it simple for now
            val routinesResult = getRoutinesUseCase()
            val streakResult = getStreakUseCase()

            routinesResult.onSuccess { routines ->
                val morning = routines.find { it.routineType == RoutineType.Morning }
                val evening = routines.find { it.routineType == RoutineType.Evening }
                
                _uiState.value = _uiState.value.copy(
                    morningRoutineStatus = if (morning != null) 
                        "${if (morning.isCompletedToday) morning.itemCount else 0}/${morning.itemCount} completed" 
                        else "No routine",
                    eveningRoutineStatus = if (evening != null) 
                        "${if (evening.isCompletedToday) evening.itemCount else 0}/${evening.itemCount} completed" 
                        else "No routine"
                )
            }

            streakResult.onSuccess { streak ->
                _uiState.value = _uiState.value.copy(
                    currentStreak = streak.currentStreak
                )
            }
            
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private fun updateTimeAndGreeting() {
        val now = LocalDateTime.now()
        val hour = now.hour
        
        val greetingRes = when (hour) {
            in 5..11 -> R.string.home_greeting_morning
            in 12..17 -> R.string.home_greeting_afternoon
            else -> R.string.home_greeting_evening
        }

        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.forLanguageTag("tr-TR"))
        val dateString = now.format(dateFormatter)

        _uiState.value = _uiState.value.copy(
            greetingRes = greetingRes,
            date = dateString
        )
    }
}
