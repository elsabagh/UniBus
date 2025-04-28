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

object NotificationUtil {
    private const val CHANNEL_ID = "help_request_channel"

    fun showNotification(context: Context, title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Help Requests",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Used for help request notifications"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(
                "navigateTo",
                AppDestination.NotificationUserDestination.route
            )
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
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

        NotificationManagerCompat.from(context).notify(1, builder.build())
    }
}


object UserNotificationPrefs {
    private const val PREF_NAME = "user_notification_prefs"
    private const val NOTIFIED_DOCUMENT_IDS_KEY = "notified_document_ids"

    fun saveNotifiedIds(context: Context, ids: Set<String>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(NOTIFIED_DOCUMENT_IDS_KEY, ids).apply()
    }

    fun getNotifiedIds(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(NOTIFIED_DOCUMENT_IDS_KEY, emptySet()) ?: emptySet()
    }
}
