package com.dermaroutine.domain.notifications

data class ReminderSettings(
    val morningEnabled: Boolean,
    val morningTime: String,
    val eveningEnabled: Boolean,
    val eveningTime: String
)
