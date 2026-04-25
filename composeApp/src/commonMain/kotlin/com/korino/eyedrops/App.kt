package com.korino.eyedrops

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.korino.eyedrops.ui.HomeScreen
import com.korino.eyedrops.viewmodel.EyeDropViewModel

@Composable
fun App(viewModel: EyeDropViewModel) {
    MaterialTheme {
        HomeScreen(viewModel)
    }
}
