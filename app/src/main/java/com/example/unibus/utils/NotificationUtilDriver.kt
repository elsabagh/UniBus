package com.example.unibus.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.unibus.MainActivity
import com.example.unibus.R
import com.example.unibus.navigation.AppDestination

object NotificationUtilDriver {
    private const val CHANNEL_ID = "driver_account_notifications"

    fun showDriverNotification(context: Context, title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Driver Account Requests",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Used for new account approval notifications"
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(
                "navigateTo",
                AppDestination.NotificationDriverDestination.route
            ) // Match navigation route
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            101,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(101, builder.build())
    }
}


object DriverNotificationPrefs {
    private const val PREF_NAME = "driver_notification_prefs"
    private const val NOTIFIED_IDS_KEY = "notified_ids"

    fun saveNotifiedIds(context: Context, ids: Set<String>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(NOTIFIED_IDS_KEY, ids).apply()
    }

    fun getNotifiedIds(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(NOTIFIED_IDS_KEY, emptySet()) ?: emptySet()
    }
}
