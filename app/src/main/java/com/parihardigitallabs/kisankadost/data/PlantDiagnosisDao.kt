package com.parihardigitallabs.kisankadost.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDiagnosisDao {
    @Query("SELECT * FROM plant_diagnosis_history ORDER BY createdAt DESC, id DESC")
    fun observeAll(): Flow<List<PlantDiagnosisEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(record: PlantDiagnosisEntity)

    @Delete
    suspend fun delete(record: PlantDiagnosisEntity)

    @Query("DELETE FROM plant_diagnosis_history")
    suspend fun clearAll()
}
