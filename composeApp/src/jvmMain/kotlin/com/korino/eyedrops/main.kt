package com.korino.eyedrops

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.korino.eyedrops.data.repository.ReminderRepositoryImpl
import com.korino.eyedrops.notification.DesktopNotificationService
import com.korino.eyedrops.viewmodel.EyeDropViewModel
import java.awt.Toolkit

fun main() {
    Toolkit.getDefaultToolkit()

    val notificationService = DesktopNotificationService()
    val repository = ReminderRepositoryImpl()
    val viewModel = EyeDropViewModel(repository, notificationService)

    application {
        val windowState = rememberWindowState(width = 480.dp, height = 520.dp)

        Window(
            onCloseRequest = {
                viewModel.onCleared()
                exitApplication()
            },
            title = "EyeDrops",
            state = windowState,
        ) {
            App(viewModel, windowState)
        }
    }
}
