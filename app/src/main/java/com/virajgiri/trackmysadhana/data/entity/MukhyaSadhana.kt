package com.virajgiri.trackmysadhana.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mukhya_sadhana")
data class MukhyaSadhana(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "daily_mala_target")
    val dailyMalaTarget: Int,
    @ColumnInfo(name = "total_mala_target")
    val totalMalaTarget: Int,
    @ColumnInfo(name = "start_date")
    val startDate: String,
    @ColumnInfo(name = "end_date")
    val endDate: String,
    @ColumnInfo(name = "mukhya_mantra_text")
    val mukhyaMantraText: String = ""
)

