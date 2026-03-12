package com.virajgiri.trackmysadhana.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "jap_entry",
    foreignKeys = [ForeignKey(
        entity = MukhyaSadhana::class,
        parentColumns = ["id"],
        childColumns = ["sadhana_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("sadhana_id")]
)
data class JapEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "sadhana_id")
    val sadhanaId: Long,
    val date: String,
    @ColumnInfo(name = "mala_count")
    val malaCount: Int,
    @ColumnInfo(name = "experience_note")
    val experienceNote: String = "",
    @ColumnInfo(name = "sub_mantra_id")
    val subMantraId: Long? = null          // null = main mukhya mantra, non-null = sub-mantra
)

