package com.parihardigitallabs.kisankadost

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.parihardigitallabs.kisankadost.data.AppDatabase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AnimalReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val recordId = inputData.getLong(KEY_RECORD_ID, -1L)
        if (recordId <= 0L) return Result.failure()

        val database = AppDatabase.getInstance(applicationContext)
        val event = database.animalHealthRecordDao().getById(recordId) ?: return Result.success()
        if (!event.veterinarianConfirmed || event.nextReminderDate.isBlank() || event.reminderNotified) return Result.success()
        val animal = database.animalRecordDao().getById(event.animalId) ?: return Result.success()

        val targetDate = runCatching { LocalDate.parse(event.nextReminderDate) }.getOrNull()
            ?: return Result.failure()
        val target = LocalDateTime.of(targetDate, LocalTime.of(9, 0))
        if (LocalDateTime.now() < target) {
            return Result.retry()
        }

        createNotificationChannel()
        val launchIntent = applicationContext.packageManager
            .getLaunchIntentForPackage(applicationContext.packageName)
        val contentIntent = launchIntent?.let {
            PendingIntent.getActivity(
                applicationContext, recordId.toInt(), it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val isHindi = applicationContext.getSharedPreferences("kisan_preferences", Context.MODE_PRIVATE)
            .getBoolean("is_hindi", true)
        val title = if (event.eventTitle.isBlank()) {
            if (isHindi) "पशु देखभाल की याद दिलाना" else "Animal care reminder"
        } else event.eventTitle
        val body = "${animal.name} • ${event.nextReminderDate}"
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .apply { if (contentIntent != null) setContentIntent(contentIntent) }
            .build()

        val notifications = NotificationManagerCompat.from(applicationContext)
        if (!notifications.areNotificationsEnabled()) return Result.success()
        return try {
            notifications.notify(recordId.toInt(), notification)
            database.animalHealthRecordDao().markReminderNotified(recordId)
            Result.success()
        } catch (_: SecurityException) {
            Result.success()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                if (applicationContext.getSharedPreferences("kisan_preferences", Context.MODE_PRIVATE).getBoolean("is_hindi", true))
                    "पशु देखभाल की सूचनाएँ" else "Animal care reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = if (applicationContext.getSharedPreferences("kisan_preferences", Context.MODE_PRIVATE).getBoolean("is_hindi", true))
                    "पशु देखभाल और पशु चिकित्सक से पुष्टि की गई तारीखों की सूचनाएँ।"
                else "Reminders entered and confirmed by the animal owner and veterinarian."
            }
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val KEY_RECORD_ID = "animal_health_record_id"
        const val CHANNEL_ID = "animal_care_reminders"
    }
}
