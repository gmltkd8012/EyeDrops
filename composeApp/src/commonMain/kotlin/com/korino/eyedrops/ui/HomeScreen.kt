package com.korino.eyedrops.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.korino.eyedrops.ui.anim.AnimatePlacementNodeElement
import com.korino.eyedrops.viewmodel.EyeDropViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    viewModel: EyeDropViewModel,
    isDarkMode: Boolean,
    isCompact: Boolean,
    onToggleTheme: () -> Unit,
    onToggleCompact: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val contentPadding by animateDpAsState(
        targetValue = if (isCompact) 16.dp else 32.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )
    val spacerHeight by animateDpAsState(
        targetValue = if (isCompact) 12.dp else 40.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    LookaheadScope {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
                    .then(AnimatePlacementNodeElement(this@LookaheadScope)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = formatTime(state.remainingSeconds),
                    fontSize = if (isCompact) 48.sp else 72.sp,
                    color = if (state.isRunning)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                )

                if (!isCompact) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = if (state.isRunning) "다음 알림까지" else "타이머 정지 중",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(Modifier.height(spacerHeight))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onToggleTheme
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Filled.WbSunny else Icons.Filled.DarkMode,
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.toggleTimer() }
                    ) {
                        Icon(
                            imageVector = if (state.isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onToggleCompact
                    ) {
                        Icon(
                            imageVector = if (isCompact) Icons.Filled.Fullscreen else Icons.Filled.FullscreenExit,
                            contentDescription = null
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = "알림 간격",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(1, 30, 60, 90).forEach { minutes ->
                            FilterChip(
                                selected = state.intervalMinutes == minutes,
                                onClick = { viewModel.updateInterval(minutes) },
                                label = { Text("${minutes}분") }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(totalSeconds: Long): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
