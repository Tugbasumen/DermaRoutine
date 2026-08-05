package com.dermaroutine.data.mapper

import com.dermaroutine.data.remote.dto.JournalEntryResponse
import com.dermaroutine.domain.model.JournalEntry

fun JournalEntryResponse.toDomain(): JournalEntry {
    return JournalEntry(
        id = id,
        content = content,
        mood = mood,
        createdAt = createdAt
    )
}
