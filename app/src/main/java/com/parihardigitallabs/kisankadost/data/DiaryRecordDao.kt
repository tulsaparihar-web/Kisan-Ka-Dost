package com.parihardigitallabs.kisankadost.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao interface DiaryRecordDao {
 @Query("SELECT * FROM diary_records ORDER BY recordDate DESC, id DESC") fun observeAll(): Flow<List<DiaryRecordEntity>>
 @Query("SELECT * FROM diary_records ORDER BY recordDate DESC, id DESC") suspend fun getAllForBackup(): List<DiaryRecordEntity>
 @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(record: DiaryRecordEntity): Long
 @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(records: List<DiaryRecordEntity>)
 @Update suspend fun update(record: DiaryRecordEntity)
 @Delete suspend fun delete(record: DiaryRecordEntity)
 @Query("DELETE FROM diary_records") suspend fun clearAll()
 @Query("SELECT * FROM diary_records WHERE id = :id LIMIT 1") suspend fun getById(id: Long): DiaryRecordEntity?
 @Query("SELECT * FROM diary_records WHERE reminderDate != ''") suspend fun getReminderRecords(): List<DiaryRecordEntity>
}
