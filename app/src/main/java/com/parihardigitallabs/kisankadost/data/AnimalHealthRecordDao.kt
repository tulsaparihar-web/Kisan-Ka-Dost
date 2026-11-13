package com.parihardigitallabs.kisankadost.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao interface AnimalHealthRecordDao {
 @Query("SELECT * FROM animal_health_records ORDER BY eventDate DESC, id DESC") fun observeAll(): Flow<List<AnimalHealthRecordEntity>>
 @Query("SELECT * FROM animal_health_records ORDER BY id") suspend fun getAllForBackup(): List<AnimalHealthRecordEntity>
 @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(record: AnimalHealthRecordEntity): Long
 @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(records: List<AnimalHealthRecordEntity>)
 @Query("DELETE FROM animal_health_records") suspend fun clearAll()
 @Query("SELECT * FROM animal_health_records WHERE id = :id LIMIT 1") suspend fun getById(id: Long): AnimalHealthRecordEntity?
 @Query("SELECT * FROM animal_health_records WHERE veterinarianConfirmed = 1 AND nextReminderDate != '' AND reminderNotified = 0") suspend fun getConfirmedReminders(): List<AnimalHealthRecordEntity>
 @Query("UPDATE animal_health_records SET reminderNotified = 1 WHERE id = :id") suspend fun markReminderNotified(id: Long)
 @Delete suspend fun delete(record: AnimalHealthRecordEntity)
}
