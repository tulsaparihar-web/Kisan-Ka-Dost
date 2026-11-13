package com.parihardigitallabs.kisankadost

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.parihardigitallabs.kisankadost.data.AnimalRecordEntity
import com.parihardigitallabs.kisankadost.data.AnimalHealthRecordEntity
import com.parihardigitallabs.kisankadost.data.AnimalHealthRecordRepository
import com.parihardigitallabs.kisankadost.data.AnimalRecordRepository
import com.parihardigitallabs.kisankadost.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimalRegistryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val repository = AnimalRecordRepository(database.animalRecordDao())
    private val healthRepository = AnimalHealthRecordRepository(database.animalHealthRecordDao())

    val records = repository.records.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val healthRecords = healthRepository.records.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun add(record: AnimalRecordEntity, onSaved: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.add(record)
                onSaved()
            } catch (_: Exception) {
                onError("DUPLICATE_OR_SAVE_ERROR")
            }
        }
    }

    fun update(record: AnimalRecordEntity, onSaved: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.update(record)
                onSaved()
            } catch (_: Exception) {
                onError("SAVE_ERROR")
            }
        }
    }

    fun addHealthRecord(record: AnimalHealthRecordEntity, onSaved: (Long) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val id = healthRepository.add(record)
                onSaved(id)
            } catch (_: Exception) {
                onError()
            }
        }
    }

    fun deleteHealthRecord(record: AnimalHealthRecordEntity) {
        viewModelScope.launch {
            healthRepository.delete(record)
            AnimalReminderScheduler.cancel(getApplication(), record.id)
        }
    }

    fun delete(record: AnimalRecordEntity) {
        viewModelScope.launch { repository.delete(record) }
    }
}
