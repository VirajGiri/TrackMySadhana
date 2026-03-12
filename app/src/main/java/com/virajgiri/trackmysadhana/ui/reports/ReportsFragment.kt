package com.virajgiri.trackmysadhana.ui.reports

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.virajgiri.trackmysadhana.data.database.SadhanaDatabase
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import com.virajgiri.trackmysadhana.databinding.FragmentReportsBinding

class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = SadhanaRepository(SadhanaDatabase.getInstance(requireContext()).sadhanaDao())
        val viewModel = ViewModelProvider(this, ReportsViewModelFactory(repository))[ReportsViewModel::class.java]

        setupChart()

        viewModel.stats.observe(viewLifecycleOwner) { stats ->
            binding.tvTotalMala.text      = stats.totalMala.toString()
            binding.tvDaysMaintained.text = stats.daysMaintained.toString()
            binding.tvMissedDays.text     = stats.missedDays.toString()
            binding.tvLongestStreak.text  = "${stats.longestStreak} days"
            binding.tvCurrentStreak.text  = "${stats.currentStreak} days"
            binding.tvAvgMala.text        = String.format("%.1f", stats.avgMalaPerDay)
            binding.tvEstimate.text       = stats.completionEstimateDays
        }

        viewModel.dailyTotals.observe(viewLifecycleOwner) { totals ->
            if (totals.isEmpty()) {
                binding.barChart.setNoDataText("No jap data yet")
                return@observe
            }
            val entries = totals.takeLast(30).mapIndexed { i, d -> BarEntry(i.toFloat(), d.total.toFloat()) }
            val labels  = totals.takeLast(30).map { it.date.takeLast(5) } // MM-dd

            val dataSet = BarDataSet(entries, "Mala / Day").apply {
                color        = Color.parseColor("#FFC107")
                valueTextColor = Color.parseColor("#FFC107")
                valueTextSize  = 9f
            }
            binding.barChart.apply {
                data = BarData(dataSet).apply { barWidth = 0.7f }
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                animateY(600)
                invalidate()
            }
        }
    }

    private fun setupChart() {
        binding.barChart.apply {
            description.isEnabled = false
            legend.isEnabled      = false
            setDrawGridBackground(false)
            setBackgroundColor(Color.TRANSPARENT)
            axisLeft.apply {
                textColor  = Color.parseColor("#E0C090")
                gridColor  = Color.parseColor("#33FFC107")
                axisLineColor = Color.TRANSPARENT
                setDrawAxisLine(false)
            }
            axisRight.isEnabled = false
            xAxis.apply {
                position       = XAxis.XAxisPosition.BOTTOM
                textColor      = Color.parseColor("#E0C090")
                gridColor      = Color.TRANSPARENT
                setDrawGridLines(false)
                granularity    = 1f
                labelRotationAngle = -45f
            }
            setTouchEnabled(true)
            isDragEnabled   = true
            setScaleEnabled(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

