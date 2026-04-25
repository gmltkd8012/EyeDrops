package com.korino.eyedrops.domain.repository

import com.korino.eyedrops.domain.model.ReminderState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ReminderRepository {
    val state: StateFlow<ReminderState>
    val reminderEvent: Flow<Unit>
    fun start()
    fun stop()
    fun updateInterval(minutes: Int)
}
