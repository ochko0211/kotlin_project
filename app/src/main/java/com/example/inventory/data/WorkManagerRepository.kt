package com.example.inventory.data

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.inventory.Constants
import com.example.inventory.worker.ReminderWorker
import java.util.concurrent.TimeUnit

class WorkManagerRepository(private val context: Context) {
    private val workManager = WorkManager.getInstance(context)
    private val uniqueWorkName = "periodic_word_reminder_work"

    fun schedulePeriodicReminder() {
        try {
            val data = Data.Builder()
                .putString(ReminderWorker.NOTIFICATION_TITLE_KEY, Constants.NOTIFICATION_TITLE)
                .putString(ReminderWorker.NOTIFICATION_MESSAGE_KEY, Constants.NOTIFICATION_MESSAGE)
                .build()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
                Constants.ONE_DAY, TimeUnit.HOURS
            )
                .setInitialDelay(24, TimeUnit.HOURS)
                .setInputData(data)
                .build()

            workManager.enqueueUniquePeriodicWork(
                uniqueWorkName,
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWorkRequest
            )

            Log.d("WorkManager", "Periodic reminder scheduled every 5 minutes")
        } catch (e: Exception) {
            Log.e("WorkManager", "Failed to schedule periodic reminder", e)
        }
    }
}