package com.korino.eyedrops.viewmodel

import com.korino.eyedrops.domain.model.ReminderState
import com.korino.eyedrops.domain.repository.ReminderRepository
import com.korino.eyedrops.domain.service.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EyeDropViewModel(
    private val repository: ReminderRepository,
    private val notificationService: NotificationService
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val state: StateFlow<ReminderState> = repository.state

    init {
        scope.launch {
            repository.reminderEvent.collect {
                notificationService.show(
                    title = "EyeDrops 💧",
                    message = "인공눈물 넣을 시간이에요! 잠깐 쉬어가세요."
                )
            }
        }
    }

    fun toggleTimer() {
        if (state.value.isRunning) repository.stop() else repository.start()
    }

    fun updateInterval(minutes: Int) = repository.updateInterval(minutes)

    fun onCleared() = scope.cancel()
}
