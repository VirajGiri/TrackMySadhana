package com.virajgiri.trackmysadhana.ui.dailyentry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virajgiri.trackmysadhana.data.entity.JapEntry
import com.virajgiri.trackmysadhana.databinding.ItemJapEntryBinding

class JapEntryAdapter : ListAdapter<JapEntry, JapEntryAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemJapEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: JapEntry) {
            binding.tvEntryDate.text = entry.date
            binding.tvEntryMala.text = "${entry.malaCount} Mala"
            if (entry.experienceNote.isNotBlank()) {
                binding.tvEntryNote.visibility = View.VISIBLE
                binding.tvEntryNote.text = entry.experienceNote
            } else {
                binding.tvEntryNote.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJapEntryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    private class DiffCallback : DiffUtil.ItemCallback<JapEntry>() {
        override fun areItemsTheSame(old: JapEntry, new: JapEntry) = old.id == new.id
        override fun areContentsTheSame(old: JapEntry, new: JapEntry) = old == new
    }
}

