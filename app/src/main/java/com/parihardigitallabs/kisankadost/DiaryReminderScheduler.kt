package com.parihardigitallabs.kisankadost
import android.content.Context
import androidx.work.*
import java.time.*
import java.util.concurrent.TimeUnit
object DiaryReminderScheduler {
 fun schedule(context:Context,id:Long,date:String) {
  val req=OneTimeWorkRequestBuilder<DiaryReminderWorker>()
   .setInputData(workDataOf(DiaryReminderWorker.KEY_ID to id))
   .setInitialDelay(calculateDiaryReminderDelayMillis(date,ZonedDateTime.now()),TimeUnit.MILLISECONDS)
   .setBackoffCriteria(BackoffPolicy.LINEAR,15,TimeUnit.MINUTES).build()
  WorkManager.getInstance(context.applicationContext).enqueueUniqueWork("farm_diary_reminder_$id",ExistingWorkPolicy.REPLACE,req)
 }
 fun cancel(context:Context,id:Long)=WorkManager.getInstance(context.applicationContext).cancelUniqueWork("farm_diary_reminder_$id")
}
internal fun calculateDiaryReminderDelayMillis(date:String,now:ZonedDateTime):Long {
 val target=LocalDate.parse(date).atTime(LocalTime.of(9,0)).atZone(now.zone)
 return Duration.between(now,target).toMillis().coerceAtLeast(0L)
}
