package com.dermaroutine.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.notifications.GetReminderSettingsUseCase
import com.dermaroutine.domain.notifications.UpdateReminderSettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineReminderViewModel @Inject constructor(
    private val getReminderSettingsUseCase: GetReminderSettingsUseCase,
    private val updateReminderSettingUseCase: UpdateReminderSettingUseCase
) : ViewModel() {

    val uiState: StateFlow<com.dermaroutine.domain.notifications.ReminderSettings?> = getReminderSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleMorning(enabled: Boolean) {
        viewModelScope.launch {
            updateReminderSettingUseCase.updateMorning(enabled)
        }
    }

    fun updateMorningTime(hour: Int, minute: Int) {
        val time = String.format("%02d:%02d", hour, minute)
        viewModelScope.launch {
            updateReminderSettingUseCase.updateMorning(enabled = uiState.value?.morningEnabled ?: true, time = time)
        }
    }

    fun toggleEvening(enabled: Boolean) {
        viewModelScope.launch {
            updateReminderSettingUseCase.updateEvening(enabled)
        }
    }

    fun updateEveningTime(hour: Int, minute: Int) {
        val time = String.format("%02d:%02d", hour, minute)
        viewModelScope.launch {
            updateReminderSettingUseCase.updateEvening(enabled = uiState.value?.eveningEnabled ?: true, time = time)
        }
    }
}
