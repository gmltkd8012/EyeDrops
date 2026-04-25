package com.korino.eyedrops.data.repository

import com.korino.eyedrops.domain.model.ReminderState
import com.korino.eyedrops.domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ReminderRepositoryImpl : ReminderRepository {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _state = MutableStateFlow(ReminderState())
    override val state: StateFlow<ReminderState> = _state.asStateFlow()

    private val _reminderEvent = MutableSharedFlow<Unit>()
    override val reminderEvent: Flow<Unit> = _reminderEvent.asSharedFlow()

    private var timerJob: Job? = null

    override fun start() {
        if (_state.value.isRunning) return
        _state.update { it.copy(isRunning = true, remainingSeconds = it.intervalMinutes * 60L) }
        timerJob = scope.launch {
            while (isActive) {
                delay(1000L)
                val remaining = _state.value.remainingSeconds - 1
                if (remaining <= 0) {
                    _reminderEvent.emit(Unit)
                    _state.update { it.copy(remainingSeconds = it.intervalMinutes * 60L) }
                } else {
                    _state.update { it.copy(remainingSeconds = remaining) }
                }
            }
        }
    }

    override fun stop() {
        timerJob?.cancel()
        timerJob = null
        _state.update { it.copy(isRunning = false, remainingSeconds = it.intervalMinutes * 60L) }
    }

    override fun updateInterval(minutes: Int) {
        val wasRunning = _state.value.isRunning
        stop()
        _state.update { it.copy(intervalMinutes = minutes) }
        if (wasRunning) start()
    }
}
