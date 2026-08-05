package com.dermaroutine.domain.model

data class JournalEntry(
    val id: Int,
    val content: String,
    val mood: String?,
    val createdAt: String
)
