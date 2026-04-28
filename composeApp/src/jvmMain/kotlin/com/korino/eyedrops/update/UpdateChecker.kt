package com.korino.eyedrops.update

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

const val APP_VERSION = "1.0.0"

private const val RELEASES_API =
    "https://api.github.com/repos/gmltkd8012/EyeDrops/releases/latest"

suspend fun fetchLatestVersion(): String? = withContext(Dispatchers.IO) {
    runCatching {
        val json = URL(RELEASES_API).readText()
        val tag = Regex(""""tag_name"\s*:\s*"v?([^"]+)"""")
            .find(json)?.groupValues?.get(1) ?: return@runCatching null
        if (isNewerVersion(tag, APP_VERSION)) tag else null
    }.getOrNull()
}

private fun isNewerVersion(latest: String, current: String): Boolean {
    val l = latest.split(".").mapNotNull { it.toIntOrNull() }
    val c = current.split(".").mapNotNull { it.toIntOrNull() }
    repeat(maxOf(l.size, c.size)) { i ->
        val diff = l.getOrElse(i) { 0 } - c.getOrElse(i) { 0 }
        if (diff != 0) return diff > 0
    }
    return false
}
