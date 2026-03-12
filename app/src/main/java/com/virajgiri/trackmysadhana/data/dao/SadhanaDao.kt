package com.virajgiri.trackmysadhana.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.virajgiri.trackmysadhana.data.entity.JapEntry
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.entity.SubMantra

@Dao
interface SadhanaDao {

    // ── Mukhya Sadhana ───────────────────────────────────────────────────────

    @Query("SELECT * FROM mukhya_sadhana ORDER BY id DESC")
    fun getAllSadhanas(): LiveData<List<MukhyaSadhana>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSadhana(sadhana: MukhyaSadhana): Long

    @Update
    suspend fun updateSadhana(sadhana: MukhyaSadhana)

    @Query("SELECT COUNT(*) FROM mukhya_sadhana")
    suspend fun getSadhanaCount(): Int

    @Query("SELECT * FROM mukhya_sadhana WHERE id = :id")
    suspend fun getSadhanaById(id: Long): MukhyaSadhana?

    @Delete
    suspend fun deleteSadhana(sadhana: MukhyaSadhana)

    // ── Sub Mantra ────────────────────────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubMantra(mantra: SubMantra)

    @Query("SELECT * FROM sub_mantra WHERE sadhana_id = :sadhanaId ORDER BY type ASC")
    fun getSubMantrasForSadhana(sadhanaId: Long): LiveData<List<SubMantra>>

    @Query("SELECT * FROM sub_mantra WHERE sadhana_id = :sadhanaId ORDER BY type ASC")
    suspend fun getSubMantrasOnce(sadhanaId: Long): List<SubMantra>

    @Query("DELETE FROM sub_mantra WHERE sadhana_id = :sadhanaId")
    suspend fun deleteSubMantrasForSadhana(sadhanaId: Long)

    // ── Jap Entry ─────────────────────────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJapEntry(entry: JapEntry)

    @Update
    suspend fun updateJapEntry(entry: JapEntry)

    @Delete
    suspend fun deleteJapEntry(entry: JapEntry)

    // Main mantra only (sub_mantra_id IS NULL)
    @Query("SELECT COALESCE(SUM(mala_count), 0) FROM jap_entry WHERE sadhana_id = :sadhanaId AND sub_mantra_id IS NULL")
    suspend fun getTotalMala(sadhanaId: Long): Int

    @Query("SELECT COALESCE(SUM(mala_count), 0) FROM jap_entry WHERE sadhana_id = :sadhanaId AND date = :date AND sub_mantra_id IS NULL")
    suspend fun getTodayMala(sadhanaId: Long, date: String): Int

    @Query("SELECT COUNT(DISTINCT date) FROM jap_entry WHERE sadhana_id = :sadhanaId AND sub_mantra_id IS NULL")
    suspend fun getDaysCompleted(sadhanaId: Long): Int

    // Sub-mantra specific
    @Query("SELECT COALESCE(SUM(mala_count), 0) FROM jap_entry WHERE sub_mantra_id = :subMantraId AND date = :date")
    suspend fun getSubMantraTodayMala(subMantraId: Long, date: String): Int

    @Query("SELECT COALESCE(SUM(mala_count), 0) FROM jap_entry WHERE sub_mantra_id = :subMantraId")
    suspend fun getSubMantraTotalMala(subMantraId: Long): Int

    // All entries for a sadhana (main + sub)
    @Query("SELECT * FROM jap_entry WHERE sadhana_id = :sadhanaId AND sub_mantra_id IS NULL ORDER BY date DESC")
    fun getEntriesForSadhana(sadhanaId: Long): LiveData<List<JapEntry>>

    @Query("SELECT * FROM jap_entry WHERE sadhana_id = :sadhanaId AND sub_mantra_id IS NULL AND date = :date ORDER BY id DESC")
    fun getEntriesForSadhanaOnDate(sadhanaId: Long, date: String): LiveData<List<JapEntry>>

    @Query("SELECT * FROM jap_entry ORDER BY date DESC, sadhana_id ASC")
    fun getAllEntries(): LiveData<List<JapEntry>>

    @Query("SELECT date, SUM(mala_count) as total FROM jap_entry WHERE sub_mantra_id IS NULL GROUP BY date ORDER BY date ASC")
    fun getDailyTotals(): LiveData<List<DailyTotal>>
}
