package com.dermaroutine.presentation.notifications

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun RoutineReminderSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: RoutineReminderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showMorningTimePicker by remember { mutableStateOf(false) }
    var showEveningTimePicker by remember { mutableStateOf(false) }

    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    if (showMorningTimePicker) {
        val currentTime = uiState?.morningTime?.split(":") ?: listOf("08", "00")
        TimePickerDialog(
            onDismiss = { showMorningTimePicker = false },
            onConfirm = { h, m ->
                viewModel.updateMorningTime(h, m)
                showMorningTimePicker = false
            },
            initialHour = currentTime[0].toInt(),
            initialMinute = currentTime[1].toInt()
        )
    }

    if (showEveningTimePicker) {
        val currentTime = uiState?.eveningTime?.split(":") ?: listOf("22", "00")
        TimePickerDialog(
            onDismiss = { showEveningTimePicker = false },
            onConfirm = { h, m ->
                viewModel.updateEveningTime(h, m)
                showEveningTimePicker = false
            },
            initialHour = currentTime[0].toInt(),
            initialMinute = currentTime[1].toInt()
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.reminder_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.reminder_desc),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Morning Reminder
            ReminderCard(
                title = stringResource(id = R.string.reminder_morning),
                enabled = uiState?.morningEnabled ?: false,
                time = uiState?.morningTime ?: "08:00",
                onToggle = { 
                    if (it && notificationPermissionState?.status?.isGranted == false) {
                        notificationPermissionState.launchPermissionRequest()
                    }
                    viewModel.toggleMorning(it) 
                },
                onTimeClick = { showMorningTimePicker = true }
            )

            // Evening Reminder
            ReminderCard(
                title = stringResource(id = R.string.reminder_evening),
                enabled = uiState?.eveningEnabled ?: false,
                time = uiState?.eveningTime ?: "22:00",
                onToggle = { 
                    if (it && notificationPermissionState?.status?.isGranted == false) {
                        notificationPermissionState.launchPermissionRequest()
                    }
                    viewModel.toggleEvening(it) 
                },
                onTimeClick = { showEveningTimePicker = true }
            )
            
            if (notificationPermissionState != null && !notificationPermissionState.status.isGranted) {
                Text(
                    text = stringResource(id = R.string.reminder_permission_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
    initialHour: Int,
    initialMinute: Int
) {
    val state = rememberTimePickerState(initialHour, initialMinute, is24Hour = true)
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour, state.minute) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            TimePicker(state = state)
        }
    )
}

@Composable
fun ReminderCard(
    title: String,
    enabled: Boolean,
    time: String,
    onToggle: (Boolean) -> Unit,
    onTimeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TextButton(
                    onClick = onTimeClick,
                    enabled = enabled,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = time, style = MaterialTheme.typography.headlineSmall, color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                }
            }
            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
