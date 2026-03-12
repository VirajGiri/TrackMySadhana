package com.virajgiri.trackmysadhana.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.virajgiri.trackmysadhana.data.database.SadhanaDatabase
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import com.virajgiri.trackmysadhana.databinding.FragmentDashboardBinding
import com.virajgiri.trackmysadhana.ui.addsadhana.AddSadhanaActivity
import com.virajgiri.trackmysadhana.ui.dailyentry.DailyEntryActivity
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewModel
    private lateinit var adapter: SadhanaAdapter
    private lateinit var repository: SadhanaRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = SadhanaRepository(SadhanaDatabase.getInstance(requireContext()).sadhanaDao())
        viewModel = ViewModelProvider(this, DashboardViewModelFactory(repository))[DashboardViewModel::class.java]

        adapter = SadhanaAdapter(
            onCardClick = { sadhana ->
                startActivity(Intent(requireContext(), DailyEntryActivity::class.java).apply {
                    putExtra(DailyEntryActivity.EXTRA_SADHANA_ID, sadhana.id)
                    putExtra(DailyEntryActivity.EXTRA_SADHANA_NAME, sadhana.name)
                })
            },
            onEdit = { sadhana -> openEditScreen(sadhana) },
            onDelete = { sadhana -> confirmDelete(sadhana) }
        )

        binding.rvSadhanas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSadhanas.adapter = adapter

        viewModel.sadhanas.observe(viewLifecycleOwner) { sadhanas ->
            viewModel.loadProgress(sadhanas)
        }
        viewModel.sadhanasWithProgress.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun openEditScreen(sadhana: MukhyaSadhana) {
        startActivity(Intent(requireContext(), AddSadhanaActivity::class.java).apply {
            putExtra(AddSadhanaActivity.EXTRA_SADHANA_ID, sadhana.id)
        })
    }

    private fun confirmDelete(sadhana: MukhyaSadhana) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Sadhana")
            .setMessage("Delete \"${sadhana.name}\"?\n\nAll jap entries and mantras will be permanently removed.")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    repository.deleteSadhana(sadhana)
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
