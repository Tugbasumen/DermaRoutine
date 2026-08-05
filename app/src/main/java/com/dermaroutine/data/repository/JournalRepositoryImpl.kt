package com.dermaroutine.data.repository

import com.dermaroutine.data.mapper.toDomain
import com.dermaroutine.data.remote.JournalApiService
import com.dermaroutine.data.remote.dto.CreateJournalEntryRequest
import com.dermaroutine.domain.model.JournalEntry
import com.dermaroutine.domain.repository.JournalRepository
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(
    private val apiService: JournalApiService
) : JournalRepository {

    override suspend fun getJournalEntries(): Result<List<JournalEntry>> {
        return try {
            val response = apiService.getEntries()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { it.toDomain() })
            } else {
                Result.failure(Exception("Günlük kayıtları yüklenemedi."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addJournalEntry(content: String, mood: String?): Result<JournalEntry> {
        return try {
            val request = CreateJournalEntryRequest(content, mood)
            val response = apiService.createEntry(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Günlük kaydı oluşturulamadı."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteJournalEntry(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteEntry(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Günlük kaydı silinemedi."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
