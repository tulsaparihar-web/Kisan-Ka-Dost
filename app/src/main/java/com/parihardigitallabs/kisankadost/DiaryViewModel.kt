package com.parihardigitallabs.kisankadost
import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.parihardigitallabs.kisankadost.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class DiaryViewModel(application: Application) : AndroidViewModel(application) {
 private val repo = DiaryRecordRepository(AppDatabase.getInstance(application).diaryRecordDao())
 val records = repo.records.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
 fun add(record: DiaryRecordEntity, saved:(Long)->Unit, failed:()->Unit) { viewModelScope.launch { runCatching { repo.add(record) }.onSuccess(saved).onFailure { failed() } } }
 fun update(record: DiaryRecordEntity, saved:()->Unit, failed:()->Unit) { viewModelScope.launch { runCatching { repo.update(record) }.onSuccess { saved() }.onFailure { failed() } } }
 fun delete(record: DiaryRecordEntity) { viewModelScope.launch { repo.delete(record); DiaryReminderScheduler.cancel(getApplication(),record.id) } }
 fun schedule(id:Long,date:String) = DiaryReminderScheduler.schedule(getApplication(),id,date)
 fun reschedule() { viewModelScope.launch {
  val app=getApplication<Application>()
  val allowed=Build.VERSION.SDK_INT<33 || ContextCompat.checkSelfPermission(app,Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED
  if(allowed) repo.reminderRecords().forEach { r -> if(r.reminderDate.isNotBlank()) runCatching { DiaryReminderScheduler.schedule(app,r.id,r.reminderDate) } }
 } }
}
