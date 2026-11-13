package com.parihardigitallabs.kisankadost.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalHealthRecordDao {
    @Query("SELECT * FROM animal_health_records ORDER BY eventDate DESC, id DESC")
    fun observeAll(): Flow<List<AnimalHealthRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(record: AnimalHealthRecordEntity): Long

    @Query("SELECT * FROM animal_health_records WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): AnimalHealthRecordEntity?

    @Delete
    suspend fun delete(record: AnimalHealthRecordEntity)
}
