package com.virajgiri.trackmysadhana.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sub_mantra",
    foreignKeys = [ForeignKey(
        entity = MukhyaSadhana::class,
        parentColumns = ["id"],
        childColumns = ["sadhana_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("sadhana_id")]
)
data class SubMantra(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "sadhana_id")
    val sadhanaId: Long,
    val type: String,          // "Naman" | "Awahan" | "Dhyan"
    @ColumnInfo(name = "mantra_text")
    val mantraText: String,
    @ColumnInfo(name = "daily_mala_target")
    val dailyMalaTarget: Int = 0
)

