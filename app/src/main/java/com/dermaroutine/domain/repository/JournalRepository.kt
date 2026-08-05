package com.dermaroutine.domain.repository

import com.dermaroutine.domain.model.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    suspend fun getJournalEntries(): Result<List<JournalEntry>>
    suspend fun addJournalEntry(content: String, mood: String?): Result<JournalEntry>
    suspend fun deleteJournalEntry(id: Int): Result<Unit>
}
