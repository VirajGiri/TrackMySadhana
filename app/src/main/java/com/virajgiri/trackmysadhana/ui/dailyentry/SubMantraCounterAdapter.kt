package com.virajgiri.trackmysadhana.ui.dailyentry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virajgiri.trackmysadhana.data.entity.SubMantra
import com.virajgiri.trackmysadhana.databinding.ItemSubMantraCounterBinding

class SubMantraCounterAdapter(
    private val onIncrement: (Long) -> Unit,
    private val onDecrement: (Long) -> Unit
) : ListAdapter<SubMantra, SubMantraCounterAdapter.ViewHolder>(Diff()) {

    // sessionCounts and todayTotals are updated externally via updateCounts()
    private var sessionCounts: Map<Long, Int> = emptyMap()
    private var todayTotals: Map<Long, Int>   = emptyMap()

    fun updateCounts(session: Map<Long, Int>, today: Map<Long, Int>) {
        sessionCounts = session
        todayTotals   = today
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val b: ItemSubMantraCounterBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(subMantra: SubMantra) {
            b.tvMantraType.text     = subMantra.type
            b.tvMantraText.text     = subMantra.mantraText
            b.tvSubSessionCount.text = (sessionCounts[subMantra.id] ?: 0).toString()
            b.tvSubTodayTotal.text   = (todayTotals[subMantra.id]   ?: 0).toString()

            b.btnSubIncrement.setOnClickListener { onIncrement(subMantra.id) }
            b.btnSubDecrement.setOnClickListener { onDecrement(subMantra.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemSubMantraCounterBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    private class Diff : DiffUtil.ItemCallback<SubMantra>() {
        override fun areItemsTheSame(a: SubMantra, b: SubMantra) = a.id == b.id
        override fun areContentsTheSame(a: SubMantra, b: SubMantra) = a == b
    }
}

