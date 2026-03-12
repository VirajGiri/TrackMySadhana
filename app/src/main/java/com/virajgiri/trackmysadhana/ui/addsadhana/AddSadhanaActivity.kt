package com.virajgiri.trackmysadhana.ui.addsadhana

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.virajgiri.trackmysadhana.R
import com.virajgiri.trackmysadhana.data.database.SadhanaDatabase
import com.virajgiri.trackmysadhana.data.repository.SadhanaRepository
import com.virajgiri.trackmysadhana.databinding.ActivityAddSadhanaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddSadhanaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSadhanaBinding
    private lateinit var viewModel: AddSadhanaViewModel
    private lateinit var mantraAdapter: MantraInputAdapter
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /** -1 for new, positive ID for edit */
    private var editSadhanaId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSadhanaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editSadhanaId = intent.getLongExtra(EXTRA_SADHANA_ID, -1L)
        val isEditMode = editSadhanaId > 0

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (isEditMode) "Edit Sadhana" else getString(R.string.add_sadhana_title)
        }

        val repository = SadhanaRepository(SadhanaDatabase.getInstance(this).sadhanaDao())
        viewModel = ViewModelProvider(this, AddSadhanaViewModelFactory(repository))[AddSadhanaViewModel::class.java]

        // Mantra RecyclerView
        mantraAdapter = MantraInputAdapter()
        binding.rvMantras.layoutManager = LinearLayoutManager(this)
        binding.rvMantras.adapter = mantraAdapter
        binding.rvMantras.isNestedScrollingEnabled = false

        binding.btnAddMantra.setOnClickListener { mantraAdapter.addItem() }

        // Start date picker
        binding.etStartDate.setOnClickListener { showDatePicker() }
        binding.tilStartDate.setEndIconOnClickListener { showDatePicker() }

        // Auto-compute end date when targets + start date are filled
        val autoCompute = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { tryAutoComputeEndDate() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        binding.etDailyMala.addTextChangedListener(autoCompute)
        binding.etTotalMala.addTextChangedListener(autoCompute)
        binding.etStartDate.addTextChangedListener(autoCompute)

        // In edit mode: load existing data into form
        if (isEditMode) {
            viewModel.loadSadhana(editSadhanaId)

            viewModel.existingSadhana.observe(this) { sadhana ->
                sadhana ?: return@observe
                binding.etSadhanaName.setText(sadhana.name)
                binding.etMukhyaMantra.setText(sadhana.mukhyaMantraText)
                binding.etDailyMala.setText(sadhana.dailyMalaTarget.toString())
                binding.etTotalMala.setText(sadhana.totalMalaTarget.toString())
                binding.etStartDate.setText(sadhana.startDate)
                binding.tvEndDateValue.text = sadhana.endDate
            }

            viewModel.existingMantras.observe(this) { subMantras ->
                if (subMantras.isNotEmpty()) {
                    mantraAdapter.setItems(subMantras.map {
                        MantraInput(it.type, it.mantraText, it.dailyMalaTarget)
                    })
                }
            }

            binding.btnSave.text = "Update Sadhana"
        }

        binding.btnSave.setOnClickListener { validateAndSave() }
    }

    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.hint_start_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        picker.addOnPositiveButtonClickListener { ms ->
            binding.etStartDate.setText(dateFormat.format(Date(ms)))
        }
        picker.show(supportFragmentManager, "start_date_picker")
    }

    private fun tryAutoComputeEndDate() {
        val daily = binding.etDailyMala.text?.toString()?.trim()?.toIntOrNull() ?: return
        val total = binding.etTotalMala.text?.toString()?.trim()?.toIntOrNull() ?: return
        val start = binding.etStartDate.text?.toString()?.trim()?.takeIf { it.isNotEmpty() } ?: return
        if (daily <= 0 || total <= 0) return
        val end = viewModel.computeEndDate(daily, total, start)
        if (end.isNotEmpty()) binding.tvEndDateValue.text = end
    }

    private fun validateAndSave() {
        val name         = binding.etSadhanaName.text?.toString()?.trim() ?: ""
        val mukhyaMantra = binding.etMukhyaMantra.text?.toString()?.trim() ?: ""
        val daily        = binding.etDailyMala.text?.toString()?.trim()?.toIntOrNull() ?: 0
        val total        = binding.etTotalMala.text?.toString()?.trim()?.toIntOrNull() ?: 0
        val start        = binding.etStartDate.text?.toString()?.trim() ?: ""
        val end          = binding.tvEndDateValue.text?.toString()?.trim() ?: ""

        listOf(binding.tilSadhanaName, binding.tilDailyMala, binding.tilTotalMala, binding.tilStartDate)
            .forEach { it.error = null }

        var valid = true
        if (name.isEmpty())  { binding.tilSadhanaName.error = " "; valid = false }
        if (daily <= 0)      { binding.tilDailyMala.error = " "; valid = false }
        if (total <= 0)      { binding.tilTotalMala.error = " "; valid = false }
        if (start.isEmpty()) { binding.tilStartDate.error = " "; valid = false }
        if (end.isEmpty() || end == "—") {
            Toast.makeText(this, "Fill targets and start date to auto-calculate end date", Toast.LENGTH_SHORT).show()
            return
        }
        if (!valid) { Toast.makeText(this, R.string.error_fill_all_fields, Toast.LENGTH_SHORT).show(); return }

        currentFocus?.clearFocus()
        binding.btnSave.isEnabled = false
        viewModel.saveSadhana(
            sadhanaId       = editSadhanaId,
            name            = name,
            mukhyaMantraText = mukhyaMantra,
            dailyTarget     = daily,
            totalTarget     = total,
            startDate       = start,
            endDate         = end,
            mantras         = mantraAdapter.getItems()
        ) {
            runOnUiThread {
                val msg = if (editSadhanaId > 0) "Sadhana updated!" else getString(R.string.sadhana_saved)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    companion object {
        const val EXTRA_SADHANA_ID = "EXTRA_SADHANA_ID"
    }
}
