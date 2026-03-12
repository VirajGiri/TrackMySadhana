package com.virajgiri.trackmysadhana.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: SadhanaRepository) : ViewModel() {

    val sadhanas: LiveData<List<MukhyaSadhana>> = repository.getAllSadhanas()

    private val _sadhanasWithProgress = MutableLiveData<List<SadhanaWithProgress>>()
    val sadhanasWithProgress: LiveData<List<SadhanaWithProgress>> = _sadhanasWithProgress

    fun loadProgress(sadhanas: List<MukhyaSadhana>) {
        viewModelScope.launch {
            val list = sadhanas.map { sadhana ->
                val totalMala = repository.getTotalMala(sadhana.id)
                val daysCompleted = repository.getDaysCompleted(sadhana.id)
                val percent = if (sadhana.totalMalaTarget > 0)
                    ((totalMala.toFloat() / sadhana.totalMalaTarget) * 100).toInt().coerceIn(0, 100)
                else 0
                SadhanaWithProgress(
                    sadhana = sadhana,
                    totalCompletedMala = totalMala,
                    daysCompleted = daysCompleted,
                    completionPercent = percent
                )
            }
            _sadhanasWithProgress.postValue(list)
        }
    }
}

class DashboardViewModelFactory(
    private val repository: SadhanaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

