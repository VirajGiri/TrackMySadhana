package com.virajgiri.trackmysadhana.ui.dailyentry

import androidx.lifecycle.*
import com.virajgiri.trackmysadhana.data.entity.JapEntry
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.entity.SubMantra
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import kotlinx.coroutines.launch

class DailyEntryViewModel(
    private val repository: SadhanaRepository,
    val sadhanaId: Long
) : ViewModel() {

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    val entriesForSelectedDate: LiveData<List<JapEntry>> =
        _selectedDate.switchMap { date -> repository.getEntriesForSadhanaOnDate(sadhanaId, date) }

    // Sub-mantras belonging to this sadhana
    val subMantras: LiveData<List<SubMantra>> = repository.getSubMantrasForSadhana(sadhanaId)

    private val _sadhana = MutableLiveData<MukhyaSadhana?>()
    val sadhana: LiveData<MukhyaSadhana?> = _sadhana

    // Main mantra stats
    private val _totalMala = MutableLiveData(0)
    val totalMala: LiveData<Int> = _totalMala

    private val _todayMala = MutableLiveData(0)
    val todayMala: LiveData<Int> = _todayMala

    private val _daysCompleted = MutableLiveData(0)
    val daysCompleted: LiveData<Int> = _daysCompleted

    // Main mantra session counter
    private val _sessionCount = MutableLiveData(0)
    val sessionCount: LiveData<Int> = _sessionCount

    // Sub-mantra session counters: subMantraId -> session count (not yet saved)
    private val _subMantraSessionCounts = MutableLiveData<Map<Long, Int>>(emptyMap())
    val subMantraSessionCounts: LiveData<Map<Long, Int>> = _subMantraSessionCounts

    // Sub-mantra today totals from DB: subMantraId -> count already saved today
    private val _subMantraTodayTotals = MutableLiveData<Map<Long, Int>>(emptyMap())
    val subMantraTodayTotals: LiveData<Map<Long, Int>> = _subMantraTodayTotals

    init {
        loadSadhana()
        setSelectedDate(todayDate())
    }

    fun todayDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    private fun loadSadhana() {
        viewModelScope.launch { _sadhana.postValue(repository.getSadhanaById(sadhanaId)) }
    }

    fun loadStats(date: String) {
        viewModelScope.launch {
            _totalMala.postValue(repository.getTotalMala(sadhanaId))
            _todayMala.postValue(repository.getTodayMala(sadhanaId, date))
            _daysCompleted.postValue(repository.getDaysCompleted(sadhanaId))
        }
    }

    fun loadSubMantraTodayTotals(date: String, subMantras: List<SubMantra>) {
        viewModelScope.launch {
            val map = subMantras.associate { sm ->
                sm.id to repository.getSubMantraTodayMala(sm.id, date)
            }
            _subMantraTodayTotals.postValue(map)
        }
    }

    // ── Main counter ──────────────────────────────────────────────────────────
    fun increment() { _sessionCount.value = (_sessionCount.value ?: 0) + 1 }
    fun decrement() { _sessionCount.value = ((_sessionCount.value ?: 0) - 1).coerceAtLeast(0) }
    fun resetSession() { _sessionCount.value = 0 }

    // ── Sub-mantra counters ───────────────────────────────────────────────────
    fun incrementSubMantra(subMantraId: Long) {
        val map = _subMantraSessionCounts.value?.toMutableMap() ?: mutableMapOf()
        map[subMantraId] = (map[subMantraId] ?: 0) + 1
        _subMantraSessionCounts.value = map
    }

    fun decrementSubMantra(subMantraId: Long) {
        val map = _subMantraSessionCounts.value?.toMutableMap() ?: mutableMapOf()
        map[subMantraId] = ((map[subMantraId] ?: 0) - 1).coerceAtLeast(0)
        _subMantraSessionCounts.value = map
    }

    fun resetSubMantraSessions() {
        _subMantraSessionCounts.value = emptyMap()
    }

    // ── Save ──────────────────────────────────────────────────────────────────
    fun saveEntry(date: String, mainMalaCount: Int, note: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            // Save main mantra entry (sub_mantra_id = null)
            if (mainMalaCount > 0) {
                repository.insertJapEntry(
                    JapEntry(sadhanaId = sadhanaId, date = date, malaCount = mainMalaCount, experienceNote = note)
                )
            }
            // Save each sub-mantra entry (sub_mantra_id set)
            val subCounts = _subMantraSessionCounts.value ?: emptyMap()
            subCounts.forEach { (subMantraId, count) ->
                if (count > 0) {
                    repository.insertJapEntry(
                        JapEntry(sadhanaId = sadhanaId, date = date, malaCount = count, subMantraId = subMantraId)
                    )
                }
            }
            loadStats(date)
            // Refresh sub-mantra today totals
            val subs = subMantras.value ?: emptyList()
            if (subs.isNotEmpty()) loadSubMantraTodayTotals(date, subs)
            onComplete()
        }
    }


    fun setSelectedDate(date: String) {
        _selectedDate.value = date
        loadStats(date)
    }
}

class DailyEntryViewModelFactory(
    private val repository: SadhanaRepository,
    private val sadhanaId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DailyEntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return DailyEntryViewModel(repository, sadhanaId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
