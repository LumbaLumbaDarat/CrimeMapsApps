package com.harifrizki.crimemapsappsuser.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.harifrizki.core.utils.REQUEST_CODE_FOR_GEOFENCE_PENDING_INTENT
import com.harifrizki.crimemapsappsuser.BuildConfig
import com.harifrizki.crimemapsappsuser.R
import kotlin.random.Random

class NotificationHelper(base: Context?) : ContextWrapper(base) {

    private val channelName = getString(R.string.app_name)
    private val channelId = BuildConfig.APPLICATION_ID.plus(channelName)

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannels() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            enableVibration(true)
            description =  getString(R.string.description_channel_crime_maps_apps)
            lightColor = Color.RED
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        manager.apply {
            createNotificationChannel(notificationChannel)
        }
    }

    fun sendHighPriorityNotification(title: String?, summary: String?, body: String?, activityName: Any?) {
        val pendingIntent = PendingIntent
            .getActivity(
                this,
                REQUEST_CODE_FOR_GEOFENCE_PENDING_INTENT,
                Intent(this, activityName!!::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_launcher_background)
            priority = NotificationCompat.PRIORITY_HIGH
            setStyle(NotificationCompat
                .BigTextStyle()
                .setSummaryText(summary)
                .setBigContentTitle(title)
                .bigText(body))
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        NotificationManagerCompat.from(this).notify(Random.nextInt(), notification.build())
    }
}