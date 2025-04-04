package com.example.inventory.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.inventory.Constants
import com.example.inventory.MainActivity
import com.example.inventory.R

/**
 * Helper class for creating and showing notifications with proper channel setup
 * and full-screen display for maximum visibility
 */
class NotificationReminder(private val context: Context) {
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = Constants.CHANNEL_DESCRIPTION
                setShowBadge(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                enableLights(true)
                lightColor = android.graphics.Color.GREEN

                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

                setSound(null, null)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showPopupNotification(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!notificationManager.areNotificationsEnabled()) {
                return
            }
        }

        val pendingIntent = createPendingIntent(context)

        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))
            .setLights(android.graphics.Color.GREEN, 1000, 1000)
            .setFullScreenIntent(pendingIntent, true)
            .setOngoing(false)
            .setOnlyAlertOnce(false) // Alert every time
            .build()
        notificationManager.notify(Constants.NOTIFICATION_ID, notification)
    }

    companion object {
        /**
         * Creates a PendingIntent for opening the app when notification is clicked
         */
        private fun createPendingIntent(appContext: Context): PendingIntent {
            val intent = Intent(appContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("from_notification", true)
            }

            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            return PendingIntent.getActivity(
                appContext,
                Constants.REQUEST_CODE,
                intent,
                flags
            )
        }

        /**
         * Extension function for simpler notification creation
         */
        fun Context.makeWordReminderNotification(title: String, message: String) {
            NotificationReminder(this).showPopupNotification(title, message)
        }
    }
}