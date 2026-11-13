package com.parihardigitallabs.kisankadost
import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.parihardigitallabs.kisankadost.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class DiaryViewModel(application: Application) : AndroidViewModel(application) {
 private val app = application
 private val db = AppDatabase.getInstance(application)
 private val repo = DiaryRecordRepository(db.diaryRecordDao())
 val records = repo.records.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
 init {
  viewModelScope.launch(Dispatchers.IO) {
   val referenced = db.diaryRecordDao().getAllForBackup()
    .flatMap { it.photoPaths.lineSequence().filter(String::isNotBlank).toList() }.toSet()
   DiaryPhotoStore.pruneUnused(app, referenced)
  }
 }
 fun add(record: DiaryRecordEntity, saved:(Long)->Unit, failed:()->Unit) { viewModelScope.launch { runCatching { repo.add(record) }.onSuccess(saved).onFailure { failed() } } }
 fun update(record: DiaryRecordEntity, saved:()->Unit, failed:()->Unit) { viewModelScope.launch { runCatching { repo.update(record) }.onSuccess { if(record.reminderDate.isBlank()) DiaryReminderScheduler.cancel(app,record.id); saved() }.onFailure { failed() } } }
 fun delete(record: DiaryRecordEntity) { viewModelScope.launch { repo.delete(record); DiaryReminderScheduler.cancel(app,record.id); record.photoPaths.lineSequence().filter { it.isNotBlank() }.forEach { DiaryPhotoStore.delete(app,it) } } }
 fun schedule(id:Long,date:String) = DiaryReminderScheduler.schedule(app,id,date)
 fun exportBackup(uri:Uri, done:()->Unit, failed:(String)->Unit) { viewModelScope.launch {
  runCatching { DiaryBackupManager.exportBackup(app,uri,db) }.onSuccess { done() }.onFailure { failed(it.message ?: "Backup could not be created") }
 } }
 fun restoreBackup(uri:Uri, done:(DiaryBackupManager.RestoreSummary)->Unit, failed:(String)->Unit) { viewModelScope.launch {
  runCatching { DiaryBackupManager.restoreBackup(app,uri,db) }.onSuccess { done(it) }.onFailure { failed(it.message ?: "Backup could not be restored") }
 } }
 fun reschedule() { viewModelScope.launch {
  val allowed=Build.VERSION.SDK_INT<33 || ContextCompat.checkSelfPermission(app,Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED
  if(allowed) repo.reminderRecords().forEach { r -> if(r.reminderDate.isNotBlank()) runCatching { DiaryReminderScheduler.schedule(app,r.id,r.reminderDate) } }
 } }
}
