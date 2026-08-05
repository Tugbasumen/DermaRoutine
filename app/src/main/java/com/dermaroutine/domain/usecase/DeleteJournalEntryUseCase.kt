package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.repository.JournalRepository
import javax.inject.Inject

class DeleteJournalEntryUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteJournalEntry(id)
    }
}
