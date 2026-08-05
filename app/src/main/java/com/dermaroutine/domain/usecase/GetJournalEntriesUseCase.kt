package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.JournalEntry
import com.dermaroutine.domain.repository.JournalRepository
import javax.inject.Inject

class GetJournalEntriesUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(): Result<List<JournalEntry>> {
        return repository.getJournalEntries()
    }
}
