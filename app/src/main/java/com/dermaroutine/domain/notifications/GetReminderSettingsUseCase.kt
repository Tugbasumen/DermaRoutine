package com.dermaroutine.domain.notifications

import com.dermaroutine.core.util.RoutineReminderPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetReminderSettingsUseCase @Inject constructor(
    private val preferences: RoutineReminderPreferences
) {
    operator fun invoke(): Flow<ReminderSettings> {
        return kotlinx.coroutines.flow.combine(
            preferences.morningEnabled,
            preferences.morningTime,
            preferences.eveningEnabled,
            preferences.eveningTime
        ) { mEnabled, mTime, eEnabled, eTime ->
            ReminderSettings(mEnabled, mTime, eEnabled, eTime)
        }
    }
}
