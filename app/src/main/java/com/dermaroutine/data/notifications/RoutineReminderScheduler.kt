package com.dermaroutine.data.notifications

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoutineReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    companion object {
        const val MORNING_WORK_NAME = "morning_routine_reminder"
        const val EVENING_WORK_NAME = "evening_routine_reminder"
    }

    fun scheduleMorningReminder(time: String) {
        scheduleReminder(MORNING_WORK_NAME, RoutineReminderWorker.TYPE_MORNING, time)
    }

    fun scheduleEveningReminder(time: String) {
        scheduleReminder(EVENING_WORK_NAME, RoutineReminderWorker.TYPE_EVENING, time)
    }

    fun cancelMorningReminder() {
        workManager.cancelUniqueWork(MORNING_WORK_NAME)
    }

    fun cancelEveningReminder() {
        workManager.cancelUniqueWork(EVENING_WORK_NAME)
    }

    private fun scheduleReminder(workName: String, type: String, timeStr: String) {
        val timeParts = timeStr.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        val now = LocalDateTime.now()
        var scheduledTime = now.with(LocalTime.of(hour, minute))
        
        if (scheduledTime.isBefore(now)) {
            scheduledTime = scheduledTime.plusDays(1)
        }

        val initialDelay = Duration.between(now, scheduledTime).toMinutes()

        val workRequest = PeriodicWorkRequestBuilder<RoutineReminderWorker>(24, TimeUnit.HOURS)
            .addTag(type)
            .setInputData(workDataOf(RoutineReminderWorker.KEY_REMINDER_TYPE to type))
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}
