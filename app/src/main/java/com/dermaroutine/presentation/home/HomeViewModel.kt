package com.dermaroutine.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.R
import com.dermaroutine.domain.model.RoutineType
import com.dermaroutine.domain.usecase.*
import com.dermaroutine.domain.notifications.GetReminderSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getRoutinesUseCase: GetRoutinesUseCase,
    private val getRoutineDetailUseCase: GetRoutineDetailUseCase,
    private val getStreakUseCase: GetStreakUseCase,
    private val getProgressStatsUseCase: GetProgressStatsUseCase,
    private val getReminderSettingsUseCase: GetReminderSettingsUseCase,
    private val toggleRoutineItemUseCase: ToggleRoutineItemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateTimeAndGreeting()
        observeUser()
        loadDashboardData()
        observeNextReminder()
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

    private fun observeNextReminder() {
        viewModelScope.launch {
            getReminderSettingsUseCase().collectLatest { settings ->
                val now = LocalTime.now()
                
                val morningTime = try { LocalTime.parse(settings.morningTime) } catch (e: Exception) { LocalTime.of(8, 0) }
                val eveningTime = try { LocalTime.parse(settings.eveningTime) } catch (e: Exception) { LocalTime.of(22, 0) }

                val (nextTitle, nextTime) = when {
                    settings.morningEnabled && settings.eveningEnabled -> {
                        if (now.isBefore(morningTime)) {
                            R.string.routine_morning to settings.morningTime
                        } else if (now.isBefore(eveningTime)) {
                            R.string.routine_evening to settings.eveningTime
                        } else {
                            R.string.routine_morning to settings.morningTime
                        }
                    }
                    settings.morningEnabled -> R.string.routine_morning to settings.morningTime
                    settings.eveningEnabled -> R.string.routine_evening to settings.eveningTime
                    else -> null to null
                }

                _uiState.value = _uiState.value.copy(
                    nextReminderTitleRes = nextTitle,
                    nextReminderTime = nextTime
                )
            }
        }
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val streakResult = getStreakUseCase()
            streakResult.onSuccess { streak ->
                _uiState.value = _uiState.value.copy(currentStreak = streak.currentStreak)
            }

            val statsResult = getProgressStatsUseCase()
            statsResult.onSuccess { stats ->
                _uiState.value = _uiState.value.copy(
                    skinScore = stats.skinScore,
                    weeklyActivity = stats.weeklyActivity
                )
            }

            val routinesResult = getRoutinesUseCase()
            routinesResult.onSuccess { routines ->
                val now = LocalTime.now()
                
                // Smart select which routine to feature first
                val morning = routines.find { it.routineType == RoutineType.Morning }
                val evening = routines.find { it.routineType == RoutineType.Evening }
                
                if (morning != null) {
                    getRoutineDetailUseCase(morning.id).onSuccess { detail ->
                        _uiState.value = _uiState.value.copy(morningRoutine = detail)
                    }
                }
                
                if (evening != null) {
                    getRoutineDetailUseCase(evening.id).onSuccess { detail ->
                        _uiState.value = _uiState.value.copy(eveningRoutine = detail)
                    }
                }
            }
            
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun toggleItem(itemId: Int) {
        viewModelScope.launch {
            toggleRoutineItemUseCase(itemId).onSuccess {
                loadDashboardData()
            }
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
