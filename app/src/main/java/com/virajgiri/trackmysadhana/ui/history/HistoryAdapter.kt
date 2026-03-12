package com.virajgiri.trackmysadhana.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virajgiri.trackmysadhana.data.entity.JapEntry
import com.virajgiri.trackmysadhana.databinding.ItemHistoryDateBinding
import com.virajgiri.trackmysadhana.databinding.ItemHistoryEntryBinding
import java.text.SimpleDateFormat
import java.util.Locale

sealed class HistoryListItem {
    data class DateHeader(val date: String, val totalMala: Int) : HistoryListItem()
    data class EntryItem(val entry: JapEntry, val sadhanaName: String) : HistoryListItem()
}

class HistoryAdapter(
    private val onEdit: (JapEntry) -> Unit,
    private val onDelete: (JapEntry) -> Unit
) : ListAdapter<HistoryListItem, RecyclerView.ViewHolder>(HistoryDiffCallback()) {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ENTRY  = 1
        private val INPUT_SDF   = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        private val DISPLAY_SDF = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())

        fun formatDate(raw: String): String = try {
            DISPLAY_SDF.format(INPUT_SDF.parse(raw)!!)
        } catch (e: Exception) { raw }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HistoryListItem.DateHeader -> TYPE_HEADER
        is HistoryListItem.EntryItem  -> TYPE_ENTRY
    }

    // ── Date Header ──────────────────────────────────────────────────────────

    inner class HeaderViewHolder(private val b: ItemHistoryDateBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(item: HistoryListItem.DateHeader) {
            b.tvDate.text      = formatDate(item.date)
            b.tvDateTotal.text = "${item.totalMala} Mala"
        }
    }

    // ── Entry Row ─────────────────────────────────────────────────────────────

    inner class EntryViewHolder(private val b: ItemHistoryEntryBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(item: HistoryListItem.EntryItem) {
            b.tvSadhanaName.text = item.sadhanaName
            b.tvMalaCount.text   = "${item.entry.malaCount} Mala"

            // Sub-mantra badge
            if (item.entry.subMantraId != null) {
                b.tvSubMantraBadge.visibility = View.VISIBLE
            } else {
                b.tvSubMantraBadge.visibility = View.GONE
            }

            if (item.entry.experienceNote.isNotBlank()) {
                b.tvNote.visibility = View.VISIBLE
                b.tvNote.text = item.entry.experienceNote
            } else {
                b.tvNote.visibility = View.GONE
            }
            b.btnEdit.setOnClickListener   { onEdit(item.entry) }
            b.btnDelete.setOnClickListener { onDelete(item.entry) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(ItemHistoryDateBinding.inflate(inflater, parent, false))
            else        -> EntryViewHolder(ItemHistoryEntryBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HistoryListItem.DateHeader -> (holder as HeaderViewHolder).bind(item)
            is HistoryListItem.EntryItem  -> (holder as EntryViewHolder).bind(item)
        }
    }

    private class HistoryDiffCallback : DiffUtil.ItemCallback<HistoryListItem>() {
        override fun areItemsTheSame(old: HistoryListItem, new: HistoryListItem): Boolean {
            return when {
                old is HistoryListItem.DateHeader && new is HistoryListItem.DateHeader -> old.date == new.date
                old is HistoryListItem.EntryItem  && new is HistoryListItem.EntryItem  -> old.entry.id == new.entry.id
                else -> false
            }
        }
        override fun areContentsTheSame(old: HistoryListItem, new: HistoryListItem) = old == new
    }
}
