package com.virajgiri.trackmysadhana.ui.history

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.virajgiri.trackmysadhana.R
import com.virajgiri.trackmysadhana.data.database.SadhanaDatabase
import com.virajgiri.trackmysadhana.data.entity.JapEntry
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import com.virajgiri.trackmysadhana.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryAdapter
    private lateinit var repository: SadhanaRepository
    private var sadhanaMap: Map<Long, String> = emptyMap()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

        // Build a sadhana name map first, then observe entries
        repository.getAllSadhanas().observe(viewLifecycleOwner) { sadhanas ->
            sadhanaMap = sadhanas.associate { it.id to it.name }
            observeEntries()
        }
    }

    private fun observeEntries() {
        repository.getAllEntries().observe(viewLifecycleOwner) { entries ->
            val items = buildHistoryList(entries)
            adapter.submitList(items)
            binding.tvNoHistory.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun buildHistoryList(entries: List<JapEntry>): List<HistoryListItem> {
        val result = mutableListOf<HistoryListItem>()
        var lastDate = ""
        entries.forEach { entry ->
            if (entry.date != lastDate) {
                result.add(HistoryListItem.DateHeader(entry.date))
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
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_edit_entry))
            .setMessage("Sadhana: ${sadhanaMap[entry.sadhanaId]}\nDate: ${entry.date}")
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


