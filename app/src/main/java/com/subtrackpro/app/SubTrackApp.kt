package com.subtrackpro.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.subtrackpro.app.workers.ReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SubTrackApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var scheduler: ReminderScheduler

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        scheduler.scheduleDailyReminderCheck()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Subscription Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Renewal reminders" }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    companion object { const val CHANNEL_ID = "subtrack_channel" }
}
