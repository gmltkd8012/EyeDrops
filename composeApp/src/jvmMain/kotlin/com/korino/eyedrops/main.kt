package com.korino.eyedrops

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.korino.eyedrops.data.repository.ReminderRepositoryImpl
import com.korino.eyedrops.notification.DesktopNotificationService
import com.korino.eyedrops.update.fetchLatestVersion
import com.korino.eyedrops.viewmodel.EyeDropViewModel
import java.awt.Toolkit

fun main() {
    Toolkit.getDefaultToolkit()

    val notificationService = DesktopNotificationService()
    val repository = ReminderRepositoryImpl()
    val viewModel = EyeDropViewModel(repository, notificationService)

    application {
        var latestVersion by remember { mutableStateOf<String?>(null) }
        val windowState = rememberWindowState(width = 480.dp, height = 520.dp)

        LaunchedEffect(Unit) {
            latestVersion = fetchLatestVersion()
        }

        Window(
            onCloseRequest = {
                viewModel.onCleared()
                exitApplication()
            },
            title = "EyeDrops",
            state = windowState,
        ) {
            App(viewModel, windowState, latestVersion)
        }
    }
}
