package com.virajgiri.trackmysadhana.ui.dashboard

import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana

data class SadhanaWithProgress(
    val sadhana: MukhyaSadhana,
    val totalCompletedMala: Int,
    val daysCompleted: Int,
    val completionPercent: Int
)

