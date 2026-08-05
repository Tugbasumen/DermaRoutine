package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.JournalEntry
import com.dermaroutine.domain.repository.JournalRepository
import javax.inject.Inject

class AddJournalEntryUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(content: String, mood: String?): Result<JournalEntry> {
        return repository.addJournalEntry(content, mood)
    }
}
