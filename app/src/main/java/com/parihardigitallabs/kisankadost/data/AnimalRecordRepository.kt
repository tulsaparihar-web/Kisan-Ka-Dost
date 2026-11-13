package com.parihardigitallabs.kisankadost.data

import kotlinx.coroutines.flow.Flow

class AnimalRecordRepository(private val dao: AnimalRecordDao) {
    val records: Flow<List<AnimalRecordEntity>> = dao.observeAll()
    val count: Flow<Int> = dao.observeCount()

    suspend fun add(record: AnimalRecordEntity) {
        dao.insert(record.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
    }

    suspend fun update(record: AnimalRecordEntity) {
        dao.update(record.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun delete(record: AnimalRecordEntity) = dao.delete(record)
}
