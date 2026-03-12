package com.virajgiri.trackmysadhana.ui.dailyentry

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.virajgiri.trackmysadhana.R
import com.virajgiri.trackmysadhana.data.database.SadhanaDatabase
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import com.virajgiri.trackmysadhana.databinding.ActivityDailyEntryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyEntryBinding
    private lateinit var viewModel: DailyEntryViewModel
    private lateinit var entryAdapter: JapEntryAdapter
    private lateinit var subMantraAdapter: SubMantraCounterAdapter
    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sadhanaId   = intent.getLongExtra(EXTRA_SADHANA_ID, -1L)
        val sadhanaName = intent.getStringExtra(EXTRA_SADHANA_NAME) ?: "Sadhana"

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply { title = sadhanaName; setDisplayHomeAsUpEnabled(true) }

        val repository = SadhanaRepository(SadhanaDatabase.getInstance(this).sadhanaDao())
        viewModel = ViewModelProvider(this, DailyEntryViewModelFactory(repository, sadhanaId))[DailyEntryViewModel::class.java]

        binding.tvTodayDate.text = "Today: $today"

        // ── Sub-mantra counters ──────────────────────────────────────────────
        subMantraAdapter = SubMantraCounterAdapter(
            onIncrement = { viewModel.incrementSubMantra(it) },
            onDecrement = { viewModel.decrementSubMantra(it) }
        )
        binding.rvSubMantras.layoutManager = LinearLayoutManager(this)
        binding.rvSubMantras.adapter = subMantraAdapter
        binding.rvSubMantras.isNestedScrollingEnabled = false

        viewModel.subMantras.observe(this) { subs ->
            val hasSubs = subs.isNotEmpty()
            binding.tvSubMantraLabel.visibility = if (hasSubs) View.VISIBLE else View.GONE
            binding.rvSubMantras.visibility     = if (hasSubs) View.VISIBLE else View.GONE
            binding.dividerMain.visibility      = if (hasSubs) View.VISIBLE else View.GONE
            subMantraAdapter.submitList(subs)
            if (hasSubs) viewModel.loadSubMantraTodayTotals(today, subs)
        }

        // Refresh counter cells whenever session counts or today totals change
        viewModel.subMantraSessionCounts.observe(this) { session ->
            subMantraAdapter.updateCounts(session, viewModel.subMantraTodayTotals.value ?: emptyMap())
        }
        viewModel.subMantraTodayTotals.observe(this) { today ->
            subMantraAdapter.updateCounts(viewModel.subMantraSessionCounts.value ?: emptyMap(), today)
        }

        // ── Main mantra (Mukhya) ─────────────────────────────────────────────
        entryAdapter = JapEntryAdapter()
        binding.rvPastEntries.layoutManager = LinearLayoutManager(this)
        binding.rvPastEntries.adapter = entryAdapter

        viewModel.sadhana.observe(this) { s ->
            s ?: return@observe
            binding.tvTargetInfo.text = "Daily Target: ${s.dailyMalaTarget} Mala  •  Total: ${s.totalMalaTarget} Mala"
            if (s.mukhyaMantraText.isNotBlank()) {
                binding.tvMukhyaMantraText.visibility = android.view.View.VISIBLE
                binding.tvMukhyaMantraText.text = "✦ ${s.mukhyaMantraText}"
            } else {
                binding.tvMukhyaMantraText.visibility = android.view.View.GONE
            }
            updateTodayProgress(viewModel.todayMala.value ?: 0, s.dailyMalaTarget)
            updateOverallProgress(viewModel.totalMala.value ?: 0, s.totalMalaTarget)
        }

        viewModel.todayMala.observe(this) { done ->
            binding.tvTodayCompleted.text = "Saved Today: $done Mala"
            updateTodayProgress(done, viewModel.sadhana.value?.dailyMalaTarget ?: 1)
        }

        viewModel.totalMala.observe(this) { total ->
            updateOverallProgress(total, viewModel.sadhana.value?.totalMalaTarget ?: 1)
        }

        viewModel.daysCompleted.observe(this) { days ->
            binding.tvDaysCompleted.text = "$days days maintained"
        }

        viewModel.sessionCount.observe(this) { count ->
            binding.tvSessionCount.text = count.toString()
        }

        binding.btnIncrement.setOnClickListener { viewModel.increment() }
        binding.btnDecrement.setOnClickListener { viewModel.decrement() }

        viewModel.entries.observe(this) { entries ->
            entryAdapter.submitList(entries)
            binding.tvNoEntries.visibility = if (entries.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.btnSaveEntry.setOnClickListener { saveEntry() }
    }

    private fun updateTodayProgress(done: Int, target: Int) {
        val pct = if (target > 0) ((done.toFloat() / target) * 100).toInt().coerceIn(0, 100) else 0
        binding.progressBarToday.progress = pct
        binding.tvTodayPercent.text = "$pct%"
    }

    private fun updateOverallProgress(total: Int, target: Int) {
        val pct = if (target > 0) ((total.toFloat() / target) * 100).toInt().coerceIn(0, 100) else 0
        binding.tvTotalMala.text = "$total / $target Mala"
        binding.progressBarTotal.progress = pct
        binding.tvProgressPercent.text = "$pct%"
    }

    private fun saveEntry() {
        val mainCount = viewModel.sessionCount.value ?: 0
        val subCounts = viewModel.subMantraSessionCounts.value ?: emptyMap()
        val note      = binding.etExperienceNote.text?.toString()?.trim() ?: ""

        val hasAnything = mainCount > 0 || subCounts.values.any { it > 0 }
        if (!hasAnything) {
            Toast.makeText(this, "Count at least 1 Mala before saving", Toast.LENGTH_SHORT).show()
            return
        }
        binding.btnSaveEntry.isEnabled = false
        viewModel.saveEntry(today, mainCount, note) {
            runOnUiThread {
                Toast.makeText(this, R.string.entry_saved, Toast.LENGTH_SHORT).show()
                viewModel.resetSession()
                binding.etExperienceNote.text?.clear()
                binding.btnSaveEntry.isEnabled = true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    companion object {
        const val EXTRA_SADHANA_ID   = "SADHANA_ID"
        const val EXTRA_SADHANA_NAME = "SADHANA_NAME"
    }
}
