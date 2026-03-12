package com.virajgiri.trackmysadhana.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virajgiri.trackmysadhana.R
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.databinding.ItemSadhanaCardBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SadhanaAdapter(
    private val onCardClick: (MukhyaSadhana) -> Unit,
    private val onEdit: (MukhyaSadhana) -> Unit,
    private val onDelete: (MukhyaSadhana) -> Unit
) : ListAdapter<SadhanaWithProgress, SadhanaAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemSadhanaCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SadhanaWithProgress) {
            val sadhana = item.sadhana

            binding.tvSadhanaName.text = sadhana.name
            binding.tvMalaProgress.text =
                "${item.totalCompletedMala} / ${sadhana.totalMalaTarget} Mala"
            binding.tvDaysProgress.text =
                "${item.daysCompleted} / ${totalDays(sadhana)} Days"
            binding.progressBar.progress = item.completionPercent
            binding.tvCompletionPercent.text = "${item.completionPercent}% Completed"

            binding.root.setOnClickListener { onCardClick(sadhana) }

            binding.btnOverflow.setOnClickListener { anchor ->
                val popup = PopupMenu(anchor.context, anchor)
                popup.inflate(R.menu.menu_sadhana_card)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit   -> { onEdit(sadhana);   true }
                        R.id.action_delete -> { onDelete(sadhana); true }
                        else -> false
                    }
                }
                popup.show()
            }
        }

        private fun totalDays(sadhana: MukhyaSadhana): Int = try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val diff = sdf.parse(sadhana.endDate)!!.time - sdf.parse(sadhana.startDate)!!.time
            ((diff / 86_400_000L) + 1).toInt().coerceAtLeast(0)
        } catch (e: Exception) { 0 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemSadhanaCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    private class DiffCallback : DiffUtil.ItemCallback<SadhanaWithProgress>() {
        override fun areItemsTheSame(old: SadhanaWithProgress, new: SadhanaWithProgress) = old.sadhana.id == new.sadhana.id
        override fun areContentsTheSame(old: SadhanaWithProgress, new: SadhanaWithProgress) = old == new
    }
}
