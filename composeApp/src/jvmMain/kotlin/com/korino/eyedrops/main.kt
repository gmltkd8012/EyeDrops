package com.korino.eyedrops

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.korino.eyedrops.data.repository.ReminderRepositoryImpl
import com.korino.eyedrops.notification.DesktopNotificationService
import com.korino.eyedrops.viewmodel.EyeDropViewModel

fun main() {
    val notificationService = DesktopNotificationService()
    val repository = ReminderRepositoryImpl()
    val viewModel = EyeDropViewModel(repository, notificationService)

    application {
        Window(
            onCloseRequest = {
                viewModel.onCleared()
                exitApplication()
            },
            title = "EyeDrops",
        ) {
            App(viewModel)
        }
    }
}
