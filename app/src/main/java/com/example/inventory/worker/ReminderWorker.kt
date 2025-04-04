package com.example.inventory.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.inventory.Constants

// ReminderWorker.kt
class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {

            val title = inputData.getString(NOTIFICATION_TITLE_KEY) ?: Constants.NOTIFICATION_TITLE
            val message = inputData.getString(NOTIFICATION_MESSAGE_KEY) ?: Constants.NOTIFICATION_MESSAGE

            Log.d("ReminderWorker", "ажиллах гэж байна: $title - $message")
            NotificationReminder(applicationContext).showPopupNotification(title, message)
            Log.d("ReminderWorker", "Notification амжилттай")

            Result.success()
        } catch (e: Exception) {
            Log.e("ReminderWorker", "Notification алдаа гарсан", e)
            Result.failure()
        }
    }

    companion object {
        const val NOTIFICATION_TITLE_KEY = "NOTIFICATION_TITLE"
        const val NOTIFICATION_MESSAGE_KEY = "NOTIFICATION_MESSAGE"
    }
}