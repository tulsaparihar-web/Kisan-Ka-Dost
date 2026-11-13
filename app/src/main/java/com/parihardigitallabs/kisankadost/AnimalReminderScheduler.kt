package com.parihardigitallabs.kisankadost

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object AnimalReminderScheduler {
    fun schedule(context: Context, recordId: Long, dateIso: String) {
        val date = LocalDate.parse(dateIso)
        val target = LocalDateTime.of(date, LocalTime.of(9, 0)).atZone(ZoneId.systemDefault())
        val delay = java.time.Duration.between(java.time.ZonedDateTime.now(), target).toMillis().coerceAtLeast(0L)
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
