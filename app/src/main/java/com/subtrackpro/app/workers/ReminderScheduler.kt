package com.subtrackpro.app.workers

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(@ApplicationContext private val ctx: Context) {
    fun scheduleDailyReminderCheck() {
        val req = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
            "subtrack_reminder", ExistingPeriodicWorkPolicy.KEEP, req
        )
    }
}
