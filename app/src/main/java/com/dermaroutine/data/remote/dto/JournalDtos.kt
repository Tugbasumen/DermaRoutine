package com.dermaroutine.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateJournalEntryRequest(
    val content: String,
    val mood: String?
)

@Serializable
data class JournalEntryResponse(
    val id: Int,
    val content: String,
    val mood: String?,
    val createdAt: String
)
