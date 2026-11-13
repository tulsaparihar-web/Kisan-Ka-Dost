package com.parihardigitallabs.kisankadost.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao interface FarmerProfileDao {
 @Query("SELECT * FROM farmer_profile WHERE id = 1 LIMIT 1") fun observeProfile(): Flow<FarmerProfileEntity?>
 @Query("SELECT * FROM farmer_profile") suspend fun getAllForBackup(): List<FarmerProfileEntity>
 @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun saveProfile(profile: FarmerProfileEntity)
 @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(records: List<FarmerProfileEntity>)
 @Query("DELETE FROM farmer_profile") suspend fun clearAll()
}
