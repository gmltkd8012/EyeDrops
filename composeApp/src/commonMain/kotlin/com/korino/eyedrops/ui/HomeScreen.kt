package com.korino.eyedrops.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.korino.eyedrops.domain.model.ReminderState
import com.korino.eyedrops.viewmodel.EyeDropViewModel

@Composable
fun HomeScreen(
    viewModel: EyeDropViewModel,
    isDarkMode: Boolean,
    isCompact: Boolean,
    onToggleTheme: () -> Unit,
    onToggleCompact: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    if (isCompact) {
        CompactLayout(
            state = state,
            isDarkMode = isDarkMode,
            onToggleTimer = { viewModel.toggleTimer() },
            onToggleTheme = onToggleTheme,
            onToggleCompact = onToggleCompact
        )
    } else {
        FullLayout(
            state = state,
            isDarkMode = isDarkMode,
            onToggleTimer = { viewModel.toggleTimer() },
            onUpdateInterval = { viewModel.updateInterval(it) },
            onToggleTheme = onToggleTheme,
            onToggleCompact = onToggleCompact
        )
    }
}

@Composable
private fun FullLayout(
    state: ReminderState,
    isDarkMode: Boolean,
    onToggleTimer: () -> Unit,
    onUpdateInterval: (Int) -> Unit,
    onToggleTheme: () -> Unit,
    onToggleCompact: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 우상단 버튼 Row
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onToggleTheme) {
                Text(if (isDarkMode) "라이트모드" else "다크모드")
            }
            OutlinedButton(onClick = onToggleCompact) {
                Text("축소")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "💧 EyeDrops",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(40.dp))

            Text(
                text = formatTime(state.remainingSeconds),
                fontSize = 72.sp,
                color = if (state.isRunning)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (state.isRunning) "다음 알림까지" else "타이머 정지 중",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = onToggleTimer,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(52.dp)
            ) {
                Text(
                    text = if (state.isRunning) "정지" else "시작",
                    style = MaterialTheme.typography.titleMedium
                )
            }

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
                        onClick = { onUpdateInterval(minutes) },
                        label = { Text("${minutes}분") }
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactLayout(
    state: ReminderState,
    isDarkMode: Boolean,
    onToggleTimer: () -> Unit,
    onToggleTheme: () -> Unit,
    onToggleCompact: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formatTime(state.remainingSeconds),
            fontSize = 48.sp,
            color = if (state.isRunning)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onToggleTheme) {
                Text(if (isDarkMode) "라이트" else "다크")
            }
            OutlinedButton(onClick = onToggleCompact) {
                Text("확장")
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onToggleTimer,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(if (state.isRunning) "정지" else "시작")
        }
    }
}

private fun formatTime(totalSeconds: Long): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
