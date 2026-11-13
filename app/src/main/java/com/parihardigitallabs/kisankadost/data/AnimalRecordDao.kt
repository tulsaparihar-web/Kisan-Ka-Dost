package com.parihardigitallabs.kisankadost.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalRecordDao {
    @Query("SELECT * FROM animal_records ORDER BY updatedAt DESC, name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<AnimalRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(record: AnimalRecordEntity)

    @Update
    suspend fun update(record: AnimalRecordEntity)

    @Delete
    suspend fun delete(record: AnimalRecordEntity)

    @Query("SELECT * FROM animal_records WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): AnimalRecordEntity?

    @Query("SELECT COUNT(*) FROM animal_records")
    fun observeCount(): Flow<Int>
}
