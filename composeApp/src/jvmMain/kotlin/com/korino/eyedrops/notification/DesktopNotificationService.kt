package com.korino.eyedrops.notification

import com.korino.eyedrops.domain.service.NotificationService
import java.awt.Color
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.image.BufferedImage

class DesktopNotificationService : NotificationService {

    private var trayIcon: TrayIcon? = null

    init {
        if (SystemTray.isSupported()) setupTray()
    }

    private fun setupTray() {
        val image = BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB).apply {
            createGraphics().run {
                color = Color(0x1976D2)
                fillOval(0, 0, 16, 16)
                dispose()
            }
        }
        trayIcon = TrayIcon(image, "EyeDrops").apply { isImageAutoSize = true }
        runCatching { SystemTray.getSystemTray().add(trayIcon) }
    }

    override fun show(title: String, message: String) {
        trayIcon?.displayMessage(title, message, TrayIcon.MessageType.INFO)
    }
}
