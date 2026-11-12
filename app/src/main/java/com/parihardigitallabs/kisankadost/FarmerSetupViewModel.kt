package com.parihardigitallabs.kisankadost

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.parihardigitallabs.kisankadost.data.AppDatabase
import com.parihardigitallabs.kisankadost.data.FarmerProfileEntity
import com.parihardigitallabs.kisankadost.data.FarmerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FarmerSetupViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FarmerRepository(
        AppDatabase.getInstance(application).farmerProfileDao()
    )

    val profile = repository.profile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    fun save(profile: FarmerProfileEntity, onSaved: () -> Unit) {
        viewModelScope.launch {
            repository.save(profile)
            onSaved()
        }
    }
}
