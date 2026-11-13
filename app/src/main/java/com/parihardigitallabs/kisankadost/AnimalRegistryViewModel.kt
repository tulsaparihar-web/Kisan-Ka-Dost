package com.parihardigitallabs.kisankadost

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.parihardigitallabs.kisankadost.data.AnimalRecordEntity
import com.parihardigitallabs.kisankadost.data.AnimalRecordRepository
import com.parihardigitallabs.kisankadost.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimalRegistryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AnimalRecordRepository(AppDatabase.getInstance(application).animalRecordDao())

    val records = repository.records.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

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

    fun delete(record: AnimalRecordEntity) {
        viewModelScope.launch { repository.delete(record) }
    }
}
