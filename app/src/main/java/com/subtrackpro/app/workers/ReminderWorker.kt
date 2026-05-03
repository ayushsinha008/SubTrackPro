package com.subtrackpro.app.workers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.subtrackpro.app.MainActivity
import com.subtrackpro.app.SubTrackApp
import com.subtrackpro.app.data.repository.SubscriptionRepository
import com.subtrackpro.app.utils.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
    private val repo: SubscriptionRepository
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val now = System.currentTimeMillis()
        repo.getAll().first().forEach { sub ->
            val days = DateUtils.daysBetween(now, sub.nextBillingDate)
            if (days in 0..sub.reminderDays) notify(sub.name, days, sub.price)
        }
        return Result.success()
    }

    private fun notify(name: String, days: Int, price: Double) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(
            applicationContext, name.hashCode(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val n = NotificationCompat.Builder(applicationContext, SubTrackApp.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("$name renews in $days day(s)")
            .setContentText("Amount: ₹${"%.2f".format(price)}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true).setContentIntent(pi).build()
        ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
            ?.notify(name.hashCode(), n)
    }
}
