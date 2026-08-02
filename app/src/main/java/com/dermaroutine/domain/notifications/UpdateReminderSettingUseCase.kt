package com.dermaroutine.domain.notifications

import com.dermaroutine.core.util.RoutineReminderPreferences
import com.dermaroutine.data.notifications.RoutineReminderScheduler
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateReminderSettingUseCase @Inject constructor(
    private val preferences: RoutineReminderPreferences,
    private val scheduler: RoutineReminderScheduler
) {
    suspend fun updateMorning(enabled: Boolean, time: String? = null) {
        val newTime = time ?: preferences.morningTime.first()
        preferences.updateMorningEnabled(enabled)
        preferences.updateMorningTime(newTime)
        
        if (enabled) scheduler.scheduleMorningReminder(newTime)
        else scheduler.cancelMorningReminder()
    }

    suspend fun updateEvening(enabled: Boolean, time: String? = null) {
        val newTime = time ?: preferences.eveningTime.first()
        preferences.updateEveningEnabled(enabled)
        preferences.updateEveningTime(newTime)
        
        if (enabled) scheduler.scheduleEveningReminder(newTime)
        else scheduler.cancelEveningReminder()
    }
}
