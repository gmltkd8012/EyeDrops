package com.korino.eyedrops.notification

import com.korino.eyedrops.domain.service.NotificationService
import java.awt.Color
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.image.BufferedImage
import javax.swing.SwingUtilities

class DesktopNotificationService : NotificationService {

    private val isMac = System.getProperty("os.name").lowercase().contains("mac")
    private var trayIcon: TrayIcon? = null

    init {
        if (isMac) {
            requestMacPermission()
        } else {
            SwingUtilities.invokeLater { setupTray() }
        }
    }

    // 앱 최초 실행 시 macOS 알림 권한 요청 — osascript 첫 호출 시 권한 다이얼로그 표시
    private fun requestMacPermission() {
        runCatching {
            ProcessBuilder(
                "osascript", "-e",
                """display notification "EyeDrops 알림이 활성화되었습니다 ✓" """ +
                """with title "EyeDrops" sound name "Glass""""
            ).start()
        }
    }

    override fun show(title: String, message: String) {
        if (isMac) {
            sendMacNotification(title, message)
        } else {
            SwingUtilities.invokeLater {
                trayIcon?.displayMessage(title, message, TrayIcon.MessageType.INFO)
            }
        }
    }

    private fun sendMacNotification(title: String, message: String) {
        val safeTitle = title.replace("\"", "\\\"")
        val safeMessage = message.replace("\"", "\\\"")
        runCatching {
            ProcessBuilder(
                "osascript", "-e",
                """display notification "$safeMessage" with title "$safeTitle" sound name "Ping""""
            ).start()
        }
    }

    private fun setupTray() {
        if (!SystemTray.isSupported()) return
        runCatching {
            val image = BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB).apply {
                createGraphics().run {
                    color = Color(0x1976D2)
                    fillOval(0, 0, 16, 16)
                    dispose()
                }
            }
            trayIcon = TrayIcon(image, "EyeDrops").apply { isImageAutoSize = true }
            SystemTray.getSystemTray().add(trayIcon)
        }
    }
}
