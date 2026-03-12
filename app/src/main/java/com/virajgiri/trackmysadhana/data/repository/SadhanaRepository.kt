package com.virajgiri.trackmysadhana.data.repository

import androidx.lifecycle.LiveData
import com.virajgiri.trackmysadhana.data.dao.DailyTotal
import com.virajgiri.trackmysadhana.data.dao.SadhanaDao
import com.virajgiri.trackmysadhana.data.entity.JapEntry
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.entity.SubMantra

class SadhanaRepository(private val dao: SadhanaDao) {

    // Sadhana
    fun getAllSadhanas(): LiveData<List<MukhyaSadhana>> = dao.getAllSadhanas()
    suspend fun getSadhanaById(id: Long): MukhyaSadhana? = dao.getSadhanaById(id)
    suspend fun insertSadhana(sadhana: MukhyaSadhana) = dao.insertSadhana(sadhana)
    suspend fun updateSadhana(sadhana: MukhyaSadhana) = dao.updateSadhana(sadhana)
    suspend fun deleteSadhana(sadhana: MukhyaSadhana) = dao.deleteSadhana(sadhana)

    // SubMantra
    fun getSubMantrasForSadhana(sadhanaId: Long): LiveData<List<SubMantra>> = dao.getSubMantrasForSadhana(sadhanaId)
    suspend fun getSubMantrasOnceSuspend(sadhanaId: Long): List<SubMantra> = dao.getSubMantrasOnce(sadhanaId)
    suspend fun insertSubMantra(mantra: SubMantra) = dao.insertSubMantra(mantra)
    suspend fun deleteSubMantrasForSadhana(sadhanaId: Long) = dao.deleteSubMantrasForSadhana(sadhanaId)

    // JapEntry
    fun getEntriesForSadhana(sadhanaId: Long): LiveData<List<JapEntry>> = dao.getEntriesForSadhana(sadhanaId)
    fun getAllEntries(): LiveData<List<JapEntry>> = dao.getAllEntries()
    fun getDailyTotals(): LiveData<List<DailyTotal>> = dao.getDailyTotals()
    suspend fun getTotalMala(sadhanaId: Long): Int = dao.getTotalMala(sadhanaId)
    suspend fun getTodayMala(sadhanaId: Long, date: String): Int = dao.getTodayMala(sadhanaId, date)
    suspend fun getDaysCompleted(sadhanaId: Long): Int = dao.getDaysCompleted(sadhanaId)
    suspend fun getSubMantraTodayMala(subMantraId: Long, date: String): Int = dao.getSubMantraTodayMala(subMantraId, date)
    suspend fun getSubMantraTotalMala(subMantraId: Long): Int = dao.getSubMantraTotalMala(subMantraId)
    suspend fun insertJapEntry(entry: JapEntry) = dao.insertJapEntry(entry)
    suspend fun updateJapEntry(entry: JapEntry) = dao.updateJapEntry(entry)
    suspend fun deleteJapEntry(entry: JapEntry) = dao.deleteJapEntry(entry)
}
