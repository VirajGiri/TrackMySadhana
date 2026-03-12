package com.virajgiri.trackmysadhana.ui.history

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.virajgiri.trackmysadhana.R
import com.virajgiri.trackmysadhana.data.database.SadhanaDatabase
import com.virajgiri.trackmysadhana.data.entity.JapEntry
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import com.virajgiri.trackmysadhana.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryAdapter
    private lateinit var repository: SadhanaRepository

    private var sadhanaMap: Map<Long, String> = emptyMap()
    private var allSadhanas: List<MukhyaSadhana> = emptyList()
    private var allEntries: List<JapEntry> = emptyList()
    private var selectedSadhanaId: Long = ALL_SADHANAS

    companion object {
        private const val ALL_SADHANAS = -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = SadhanaRepository(SadhanaDatabase.getInstance(requireContext()).sadhanaDao())

        adapter = HistoryAdapter(
            onEdit   = { entry -> showEditDialog(entry) },
            onDelete = { entry -> showDeleteDialog(entry) }
        )
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter

        // Observe sadhanas → populate filter spinner
        repository.getAllSadhanas().observe(viewLifecycleOwner) { sadhanas ->
            allSadhanas = sadhanas
            sadhanaMap  = sadhanas.associate { it.id to it.name }
            setupSpinner(sadhanas)
        }

        // Observe all entries → store + rebuild list
        repository.getAllEntries().observe(viewLifecycleOwner) { entries ->
            allEntries = entries
            rebuildList()
        }
    }

    private fun setupSpinner(sadhanas: List<MukhyaSadhana>) {
        val labels = mutableListOf(getString(R.string.label_all_sadhanas))
        labels.addAll(sadhanas.map { it.name })

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            labels
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerSadhanaFilter.adapter = spinnerAdapter

        binding.spinnerSadhanaFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    selectedSadhanaId = if (pos == 0) ALL_SADHANAS else sadhanas[pos - 1].id
                    rebuildList()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun rebuildList() {
        val filtered = if (selectedSadhanaId == ALL_SADHANAS) allEntries
                       else allEntries.filter { it.sadhanaId == selectedSadhanaId }
        val items = buildHistoryList(filtered)
        adapter.submitList(items)
        binding.tvNoHistory.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE

        // Summary
        val totalMala = filtered.filter { it.subMantraId == null }.sumOf { it.malaCount }
        val days      = filtered.map { it.date }.distinct().size
        if (filtered.isNotEmpty()) {
            binding.tvHistorySummary.visibility = View.VISIBLE
            binding.tvHistorySummary.text       = "$days days  •  $totalMala Mala total"
        } else {
            binding.tvHistorySummary.visibility = View.GONE
        }
    }

    private fun buildHistoryList(entries: List<JapEntry>): List<HistoryListItem> {
        val result = mutableListOf<HistoryListItem>()
        // Group by date (entries are already sorted date DESC from DAO)
        var lastDate = ""
        var dateTotalMala = 0

        // Pre-compute main-mantra total per date for the header
        val mainTotalByDate = entries
            .filter { it.subMantraId == null }
            .groupBy { it.date }
            .mapValues { (_, v) -> v.sumOf { it.malaCount } }

        entries.forEach { entry ->
            if (entry.date != lastDate) {
                dateTotalMala = mainTotalByDate[entry.date] ?: 0
                result.add(HistoryListItem.DateHeader(entry.date, dateTotalMala))
                lastDate = entry.date
            }
            val name = sadhanaMap[entry.sadhanaId] ?: "Unknown Sadhana"
            result.add(HistoryListItem.EntryItem(entry, name))
        }
        return result
    }

    private fun showEditDialog(entry: JapEntry) {
        val editText = TextInputEditText(requireContext()).apply {
            setText(entry.malaCount.toString())
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setPadding(48, 32, 48, 32)
        }
        val sadhanaName = sadhanaMap[entry.sadhanaId] ?: "Unknown"
        val suffix = if (entry.subMantraId != null) " (Sub-mantra)" else ""
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_edit_entry))
            .setMessage("Sadhana: $sadhanaName$suffix\nDate: ${HistoryAdapter.formatDate(entry.date)}")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newCount = editText.text?.toString()?.toIntOrNull() ?: return@setPositiveButton
                if (newCount > 0) {
                    lifecycleScope.launch {
                        repository.updateJapEntry(entry.copy(malaCount = newCount))
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDialog(entry: JapEntry) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.label_delete))
            .setMessage(getString(R.string.dialog_delete_confirm))
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    repository.deleteJapEntry(entry)
                    Toast.makeText(requireContext(), "Entry deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
