package com.virajgiri.trackmysadhana.ui.addsadhana

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.entity.SubMantra
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddSadhanaViewModel(private val repository: SadhanaRepository) : ViewModel() {

    val existingSadhana = MutableLiveData<MukhyaSadhana?>()
    val existingMantras = MutableLiveData<List<SubMantra>>(emptyList())

    fun loadSadhana(sadhanaId: Long) {
        viewModelScope.launch {
            val sadhana = repository.getSadhanaById(sadhanaId)
            existingSadhana.postValue(sadhana)
            if (sadhana != null) {
                // Load sub-mantras via one-shot query workaround: observe once via DB
                existingMantras.postValue(repository.getSubMantrasOnceSuspend(sadhanaId))
            }
        }
    }

    fun computeEndDate(dailyTarget: Int, totalTarget: Int, startDate: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val start = sdf.parse(startDate) ?: return ""
            val daysNeeded = Math.ceil(totalTarget.toDouble() / dailyTarget).toInt()
            val cal = Calendar.getInstance().apply { time = start; add(Calendar.DAY_OF_YEAR, daysNeeded - 1) }
            sdf.format(cal.time)
        } catch (e: Exception) { "" }
    }

    fun saveSadhana(
        sadhanaId: Long,
        name: String,
        mukhyaMantraText: String,
        dailyTarget: Int,
        totalTarget: Int,
        startDate: String,
        endDate: String,
        mantras: List<MantraInput>,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val id: Long
            if (sadhanaId < 0) {
                id = repository.insertSadhana(
                    MukhyaSadhana(name = name, mukhyaMantraText = mukhyaMantraText,
                        dailyMalaTarget = dailyTarget, totalMalaTarget = totalTarget,
                        startDate = startDate, endDate = endDate)
                )
            } else {
                repository.updateSadhana(
                    MukhyaSadhana(id = sadhanaId, name = name, mukhyaMantraText = mukhyaMantraText,
                        dailyMalaTarget = dailyTarget, totalMalaTarget = totalTarget,
                        startDate = startDate, endDate = endDate)
                )
                id = sadhanaId
                repository.deleteSubMantrasForSadhana(id)
            }
            mantras.filter { it.text.isNotBlank() }.forEach { m ->
                repository.insertSubMantra(
                    SubMantra(sadhanaId = id, type = m.type,
                        mantraText = m.text, dailyMalaTarget = m.dailyMalaTarget)
                )
            }
            onComplete()
        }
    }
}

class AddSadhanaViewModelFactory(private val repository: SadhanaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddSadhanaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return AddSadhanaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
