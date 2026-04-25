package com.korino.eyedrops

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.korino.eyedrops.ui.HomeScreen
import com.korino.eyedrops.ui.theme.EyeDropsTheme
import com.korino.eyedrops.viewmodel.EyeDropViewModel

@Composable
fun App(viewModel: EyeDropViewModel) {
    var isDarkMode by remember { mutableStateOf(false) }
    EyeDropsTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(
                viewModel = viewModel,
                isDarkMode = isDarkMode,
                onToggleTheme = { isDarkMode = !isDarkMode }
            )
        }
    }
}
