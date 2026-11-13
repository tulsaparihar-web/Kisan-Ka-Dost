package com.parihardigitallabs.kisankadost

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object AnimalReminderScheduler {
    fun schedule(context: Context, recordId: Long, dateIso: String) {
        val delay = calculateReminderDelayMillis(dateIso, ZonedDateTime.now())
        val request = OneTimeWorkRequestBuilder<AnimalReminderWorker>()
            .setInputData(Data.Builder().putLong(AnimalReminderWorker.KEY_RECORD_ID, recordId).build())
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context.applicationContext)
            .enqueueUniqueWork("animal_health_reminder_$recordId", androidx.work.ExistingWorkPolicy.REPLACE, request)
    }

    fun cancel(context: Context, recordId: Long) {
        WorkManager.getInstance(context.applicationContext)
            .cancelUniqueWork("animal_health_reminder_$recordId")
    }
}

internal fun calculateReminderDelayMillis(dateIso: String, now: ZonedDateTime): Long {
    val target = LocalDate.parse(dateIso).atTime(LocalTime.of(9, 0)).atZone(now.zone)
    return java.time.Duration.between(now, target).toMillis().coerceAtLeast(0L)
}
