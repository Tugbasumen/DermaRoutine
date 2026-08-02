package com.dermaroutine.core.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.reminderDataStore by preferencesDataStore(name = "reminder_prefs")

@Singleton
class RoutineReminderPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val MORNING_ENABLED = booleanPreferencesKey("morning_enabled")
        val MORNING_TIME = stringPreferencesKey("morning_time")
        val EVENING_ENABLED = booleanPreferencesKey("evening_enabled")
        val EVENING_TIME = stringPreferencesKey("evening_time")
    }

    val morningEnabled: Flow<Boolean> = context.reminderDataStore.data.map { it[MORNING_ENABLED] ?: false }
    val morningTime: Flow<String> = context.reminderDataStore.data.map { it[MORNING_TIME] ?: "08:00" }
    val eveningEnabled: Flow<Boolean> = context.reminderDataStore.data.map { it[EVENING_ENABLED] ?: false }
    val eveningTime: Flow<String> = context.reminderDataStore.data.map { it[EVENING_TIME] ?: "22:00" }

    suspend fun updateMorningEnabled(enabled: Boolean) {
        context.reminderDataStore.edit { it[MORNING_ENABLED] = enabled }
    }

    suspend fun updateMorningTime(time: String) {
        context.reminderDataStore.edit { it[MORNING_TIME] = time }
    }

    suspend fun updateEveningEnabled(enabled: Boolean) {
        context.reminderDataStore.edit { it[EVENING_ENABLED] = enabled }
    }

    suspend fun updateEveningTime(time: String) {
        context.reminderDataStore.edit { it[EVENING_TIME] = time }
    }
}
