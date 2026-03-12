package com.virajgiri.trackmysadhana.ui.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.virajgiri.trackmysadhana.data.dao.DailyTotal
import com.virajgiri.trackmysadhana.data.entity.JapEntry
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ReportStats(
    val totalMala: Int,
    val daysMaintained: Int,
    val missedDays: Int,
    val longestStreak: Int,
    val currentStreak: Int,
    val avgMalaPerDay: Float,
    val completionEstimateDays: String
)

class ReportsViewModel(private val repository: SadhanaRepository) : ViewModel() {

    val dailyTotals: LiveData<List<DailyTotal>> = repository.getDailyTotals()
    val allSadhanas: LiveData<List<MukhyaSadhana>> = repository.getAllSadhanas()
    private val allEntries: LiveData<List<JapEntry>> = repository.getAllEntries()

    val stats = MediatorLiveData<ReportStats>()

    init {
        stats.addSource(allEntries)  { compute() }
        stats.addSource(allSadhanas) { compute() }
    }

    private fun compute() {
        val entries  = allEntries.value  ?: return
        val sadhanas = allSadhanas.value ?: return

        // ── Filter: only main mantra entries (sub_mantra_id IS NULL) ──────────
        val mainEntries = entries.filter { it.subMantraId == null }

        val totalMala      = mainEntries.sumOf { it.malaCount }
        val distinctDates  = mainEntries.map { it.date }.toSortedSet()
        val daysMaintained = distinctDates.size

        // ── Missed days: from earliest sadhana start to YESTERDAY ─────────────
        val sdf       = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today     = sdf.format(Date())
        val yesCal    = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val yesterday = sdf.format(yesCal.time)
        val earliestStart = sadhanas.minOfOrNull { it.startDate } ?: today
        val missedDays = try {
            if (earliestStart >= today) {
                0
            } else {
                val start     = sdf.parse(earliestStart)!!
                val end       = sdf.parse(yesterday)!!
                val totalDays = ((end.time - start.time) / 86_400_000L).toInt() + 1
                (totalDays - daysMaintained).coerceAtLeast(0)
            }
        } catch (e: Exception) { 0 }

        // ── Streaks ───────────────────────────────────────────────────────────
        val sortedDates   = distinctDates.toList()
        val longestStreak = computeLongestStreak(sortedDates)
        val currentStreak = computeCurrentStreak(sortedDates)

        // ── Average mala per active day ───────────────────────────────────────
        val avgMala = if (daysMaintained > 0) totalMala.toFloat() / daysMaintained else 0f

        // ── Completion estimate ───────────────────────────────────────────────
        val totalTarget = sadhanas.sumOf { it.totalMalaTarget }
        val remaining   = (totalTarget - totalMala).coerceAtLeast(0)
        val estimateText = if (avgMala > 0) {
            val days = (remaining / avgMala).toInt()
            "$days days"
        } else "N/A"

        stats.postValue(
            ReportStats(totalMala, daysMaintained, missedDays, longestStreak, currentStreak, avgMala, estimateText)
        )
    }

    private fun computeLongestStreak(sortedDates: List<String>): Int {
        if (sortedDates.isEmpty()) return 0
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var longest = 1; var current = 1
        for (i in 1 until sortedDates.size) {
            val prev = sdf.parse(sortedDates[i - 1])!!
            val curr = sdf.parse(sortedDates[i])!!
            val diffDays = ((curr.time - prev.time) / 86_400_000L).toInt()
            current = if (diffDays == 1) current + 1 else 1
            if (current > longest) longest = current
        }
        return longest
    }

    /** Current streak: consecutive days ending on today or yesterday. */
    private fun computeCurrentStreak(sortedDates: List<String>): Int {
        if (sortedDates.isEmpty()) return 0
        val sdf       = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today     = sdf.format(Date())
        val yesCal    = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val yesterday = sdf.format(yesCal.time)

        val lastDate = sortedDates.last()
        if (lastDate != today && lastDate != yesterday) return 0

        var streak = 1
        for (i in sortedDates.size - 2 downTo 0) {
            val curr     = sdf.parse(sortedDates[i + 1])!!
            val prev     = sdf.parse(sortedDates[i])!!
            val diffDays = ((curr.time - prev.time) / 86_400_000L).toInt()
            if (diffDays == 1) streak++ else break
        }
        return streak
    }
}

class ReportsViewModelFactory(private val repository: SadhanaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ReportsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
