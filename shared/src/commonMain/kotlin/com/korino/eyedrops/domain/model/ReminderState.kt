package com.korino.eyedrops.domain.model

data class ReminderState(
    val isRunning: Boolean = false,
    val intervalMinutes: Int = 60,
    val remainingSeconds: Long = 3600L
)
