package com.korino.eyedrops

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import com.korino.eyedrops.ui.HomeScreen
import com.korino.eyedrops.ui.theme.EyeDropsTheme
import com.korino.eyedrops.viewmodel.EyeDropViewModel

private val FullWindowSize = DpSize(480.dp, 520.dp)
private val CompactWindowSize = DpSize(300.dp, 200.dp)

@Composable
fun App(viewModel: EyeDropViewModel, windowState: WindowState) {
    var isDarkMode by remember { mutableStateOf(false) }
    var isCompact by remember { mutableStateOf(false) }

    LaunchedEffect(isCompact) {
        windowState.size = if (isCompact) CompactWindowSize else FullWindowSize
    }

    EyeDropsTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(
                viewModel = viewModel,
                isDarkMode = isDarkMode,
                isCompact = isCompact,
                onToggleTheme = { isDarkMode = !isDarkMode },
                onToggleCompact = { isCompact = !isCompact }
            )
        }
    }
}
