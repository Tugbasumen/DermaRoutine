package com.dermaroutine.data.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RoutineReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationManager: RoutineNotificationManager
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_REMINDER_TYPE = "reminder_type"
        const val TYPE_MORNING = "morning"
        const val TYPE_EVENING = "evening"
    }

    override suspend fun doWork(): Result {
        val type = inputData.getString(KEY_REMINDER_TYPE)
        
        when (type) {
            TYPE_MORNING -> notificationManager.showMorningReminder()
            TYPE_EVENING -> notificationManager.showEveningReminder()
        }

        return Result.success()
    }
}
