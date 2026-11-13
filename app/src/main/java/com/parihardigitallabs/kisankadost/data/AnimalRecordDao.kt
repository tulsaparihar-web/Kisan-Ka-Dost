package com.parihardigitallabs.kisankadost.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao interface AnimalRecordDao {
 @Query("SELECT * FROM animal_records ORDER BY updatedAt DESC, name COLLATE NOCASE ASC") fun observeAll(): Flow<List<AnimalRecordEntity>>
 @Query("SELECT * FROM animal_records ORDER BY id") suspend fun getAllForBackup(): List<AnimalRecordEntity>
 @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(record: AnimalRecordEntity)
 @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(records: List<AnimalRecordEntity>)
 @Update suspend fun update(record: AnimalRecordEntity)
 @Delete suspend fun delete(record: AnimalRecordEntity)
 @Query("DELETE FROM animal_records") suspend fun clearAll()
 @Query("SELECT * FROM animal_records WHERE id = :id LIMIT 1") suspend fun getById(id: Long): AnimalRecordEntity?
 @Query("SELECT COUNT(*) FROM animal_records") fun observeCount(): Flow<Int>
}
