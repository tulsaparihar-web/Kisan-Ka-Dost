package com.parihardigitallabs.kisankadost.data

import kotlinx.coroutines.flow.Flow

class FarmerRepository(private val dao: FarmerProfileDao) {
    val profile: Flow<FarmerProfileEntity?> = dao.observeProfile()

    suspend fun save(profile: FarmerProfileEntity) {
        dao.saveProfile(profile.copy(updatedAt = System.currentTimeMillis()))
    }
}
