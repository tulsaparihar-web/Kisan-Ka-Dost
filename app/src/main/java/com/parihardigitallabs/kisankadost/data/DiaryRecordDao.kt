package com.parihardigitallabs.kisankadost.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao interface DiaryRecordDao {
 @Query("SELECT * FROM diary_records ORDER BY recordDate DESC, id DESC") fun observeAll(): Flow<List<DiaryRecordEntity>>
 @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(record: DiaryRecordEntity): Long
 @Update suspend fun update(record: DiaryRecordEntity)
 @Delete suspend fun delete(record: DiaryRecordEntity)
 @Query("SELECT * FROM diary_records WHERE id = :id LIMIT 1") suspend fun getById(id: Long): DiaryRecordEntity?
 @Query("SELECT * FROM diary_records WHERE reminderDate != ''") suspend fun getReminderRecords(): List<DiaryRecordEntity>
}
