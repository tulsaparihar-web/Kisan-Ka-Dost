package com.parihardigitallabs.kisankadost
import android.app.*
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.parihardigitallabs.kisankadost.data.AppDatabase
import java.time.*
class DiaryReminderWorker(context:Context,params:WorkerParameters):CoroutineWorker(context,params) {
 override suspend fun doWork():Result {
  val id=inputData.getLong(KEY_ID,-1L); if(id<=0L)return Result.failure()
  val r=AppDatabase.getInstance(applicationContext).diaryRecordDao().getById(id)?:return Result.success()
  if(r.reminderDate.isBlank() || r.reminderNotified)return Result.success()
  val date=runCatching { LocalDate.parse(r.reminderDate) }.getOrNull()?:return Result.failure()
  if(LocalDateTime.now()<LocalDateTime.of(date,LocalTime.of(9,0)))return Result.retry()
  val isHindi = applicationContext.getSharedPreferences("kisan_preferences", Context.MODE_PRIVATE)
   .getBoolean("is_hindi", true)
  if(Build.VERSION.SDK_INT>=26) {
   val mgr=applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
   val channelName = if (isHindi) "खेत डायरी की सूचनाएँ" else "Farm diary reminders"
   val channelDescription = if (isHindi) "खेत डायरी में दर्ज कार्यों और तारीखों की याद दिलाने वाली सूचनाएँ।" else "Reminders for tasks and dates recorded in the farm diary."
   mgr.createNotificationChannel(NotificationChannel(CHANNEL,channelName,NotificationManager.IMPORTANCE_DEFAULT).apply {
    description = channelDescription
   })
  }
  val intent=applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName)
  val pending=intent?.let { PendingIntent.getActivity(applicationContext,id.toInt(),it,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE) }
  val body=listOf(r.subject,r.recordDate).filter { it.isNotBlank() }.joinToString(" • ")
  val note=NotificationCompat.Builder(applicationContext,CHANNEL).setSmallIcon(android.R.drawable.ic_dialog_info)
   .setContentTitle(r.title).setContentText(body.ifBlank { if (isHindi) "खेत डायरी की याद दिलाना" else "Farm diary reminder" }).setAutoCancel(true)
   .apply { if(pending!=null)setContentIntent(pending) }.build()
  val notifications = NotificationManagerCompat.from(applicationContext)
  if (!notifications.areNotificationsEnabled()) return Result.success()
  return try {
   notifications.notify(id.toInt(),note)
   AppDatabase.getInstance(applicationContext).diaryRecordDao().markReminderNotified(id)
   Result.success()
  } catch(_:SecurityException) { Result.success() }
 }
 companion object { const val KEY_ID="diary_record_id"; const val CHANNEL="farm_diary_reminders" }
}
