package com.parihardigitallabs.kisankadost.data

import kotlinx.coroutines.flow.Flow

class AnimalHealthRecordRepository(private val dao: AnimalHealthRecordDao) {
    val records: Flow<List<AnimalHealthRecordEntity>> = dao.observeAll()

    suspend fun add(record: AnimalHealthRecordEntity) {
        dao.insert(record.copy(createdAt = System.currentTimeMillis()))
    }

    suspend fun delete(record: AnimalHealthRecordEntity) {
        dao.delete(record)
    }
}
