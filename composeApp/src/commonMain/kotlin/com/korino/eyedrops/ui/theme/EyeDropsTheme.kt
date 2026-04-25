package com.korino.eyedrops.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = Cyan40,
    primaryContainer = BlueContainer40,
    background = Background40,
    surface = Surface40,
    surfaceVariant = SurfaceVariant40,
    onBackground = OnBackground40,
    onSurface = OnSurface40,
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    secondary = Cyan80,
    primaryContainer = BlueContainer80,
    background = Background80,
    surface = Surface80,
    surfaceVariant = SurfaceVariant80,
    onBackground = OnBackground80,
    onSurface = OnSurface80,
)

@Composable
fun EyeDropsTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = EyeDropsTypography,
        content = content
    )
}
