package com.parihardigitallabs.kisankadost.data
import kotlinx.coroutines.flow.Flow
class DiaryRecordRepository(private val dao: DiaryRecordDao) {
 val records: Flow<List<DiaryRecordEntity>> = dao.observeAll()
 suspend fun add(record: DiaryRecordEntity) = dao.insert(record.copy(createdAt=System.currentTimeMillis(), updatedAt=System.currentTimeMillis()))
 suspend fun update(record: DiaryRecordEntity) = dao.update(record.copy(updatedAt=System.currentTimeMillis()))
 suspend fun delete(record: DiaryRecordEntity) = dao.delete(record)
 suspend fun getById(id: Long) = dao.getById(id)
 suspend fun reminderRecords() = dao.getReminderRecords()
}
