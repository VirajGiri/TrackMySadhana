package com.virajgiri.trackmysadhana.ui.addsadhana

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virajgiri.trackmysadhana.databinding.ItemMantraInputBinding

data class MantraInput(
    var type: String = "Naman",
    var text: String = "",
    var dailyMalaTarget: Int = 0
)

private const val ADD_NEW_TYPE = "+ Add New Type"

class MantraInputAdapter : RecyclerView.Adapter<MantraInputAdapter.ViewHolder>() {

    private val items = mutableListOf<MantraInput>()
    private val mantraTypes = mutableListOf("Naman", "Awahan", "Dhyan", ADD_NEW_TYPE)

    fun getItems(): List<MantraInput> = items.toList()

    fun addItem() {
        items.add(MantraInput(type = mantraTypes.first()))
        notifyItemInserted(items.size - 1)
    }

    /** Pre-populate with existing mantras when editing a sadhana */
    fun setItems(mantras: List<MantraInput>) {
        items.clear()
        mantras.forEach { m ->
            // Register any custom types not already in the list
            if (!mantraTypes.contains(m.type) && m.type != ADD_NEW_TYPE) {
                mantraTypes.add(mantraTypes.size - 1, m.type)
            }
        }
        items.addAll(mantras)
        notifyDataSetChanged()
    }

    /** Adds a new custom type to the shared list and refreshes all dropdowns */
    private fun registerCustomType(newType: String) {
        if (newType.isNotBlank() && !mantraTypes.contains(newType)) {
            // Insert before the "Add New Type" sentinel
            mantraTypes.add(mantraTypes.size - 1, newType)
            notifyDataSetChanged() // refresh all rows so dropdowns pick up new type
        }
    }

    inner class ViewHolder(private val b: ItemMantraInputBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(item: MantraInput) {
            // ── Type dropdown ────────────────────────────────────────────────
            val adapter = ArrayAdapter(b.root.context, android.R.layout.simple_dropdown_item_1line, mantraTypes.toList())
            b.actvMantraType.setAdapter(adapter)
            b.actvMantraType.setText(item.type, false)

            b.actvMantraType.setOnItemClickListener { _, _, position, _ ->
                val selected = mantraTypes[position]
                if (selected == ADD_NEW_TYPE) {
                    // Show custom-type input, clear the dropdown display
                    b.tilCustomType.visibility = View.VISIBLE
                    b.etCustomType.requestFocus()
                    b.actvMantraType.setText(item.type, false) // revert visual to last valid type
                } else {
                    item.type = selected
                    b.tilCustomType.visibility = View.GONE
                }
            }

            // ── Custom type confirm ──────────────────────────────────────────
            val confirmCustomType = {
                val newType = b.etCustomType.text?.toString()?.trim() ?: ""
                if (newType.isNotBlank()) {
                    registerCustomType(newType)
                    item.type = newType
                    b.actvMantraType.setText(newType, false)
                }
                b.tilCustomType.visibility = View.GONE
                b.etCustomType.text?.clear()
            }
            b.etCustomType.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) { confirmCustomType(); true } else false
            }
            b.tilCustomType.setEndIconOnClickListener { confirmCustomType() }

            // ── Mantra text ──────────────────────────────────────────────────
            b.etMantraText.setText(item.text)
            b.etMantraText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) item.text = b.etMantraText.text?.toString() ?: ""
            }

            // ── Daily mala target ────────────────────────────────────────────
            if (item.dailyMalaTarget > 0) b.etSubDailyMala.setText(item.dailyMalaTarget.toString())
            b.etSubDailyMala.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) item.dailyMalaTarget = b.etSubDailyMala.text?.toString()?.toIntOrNull() ?: 0
            }

            // ── Remove ───────────────────────────────────────────────────────
            b.btnRemove.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_ID.toInt()) { items.removeAt(pos); notifyItemRemoved(pos) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemMantraInputBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size
}
