package com.example.trendingrepo.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.trendingrepo.R


class Utilities {
    companion object {
        fun sendNotification(context: Context, title: String, message: String) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //If on Oreo then notification required a notification channel.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(
                        "default",
                        "Default",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                notificationManager.createNotificationChannel(channel)
            }

            val notification: NotificationCompat.Builder = NotificationCompat.Builder(
                context,
                "default"
            )
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
            notificationManager.notify(1, notification.build())


        }
    }
}