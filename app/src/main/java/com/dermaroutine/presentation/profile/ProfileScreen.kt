package com.dermaroutine.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.R
import com.dermaroutine.core.designsystem.AppTextField
import com.dermaroutine.core.designsystem.PrimaryButton
import com.dermaroutine.core.util.getDisplayName
import com.dermaroutine.domain.model.SensitivityLevel
import com.dermaroutine.domain.model.SkinType

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToReminders: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = androidx.compose.ui.platform.LocalContext.current

    var selectedSkinType by remember { mutableStateOf(SkinType.NotSure) }
    var selectedSensitivity by remember { mutableStateOf(SensitivityLevel.Low) }
    var selectedConcerns by remember { mutableStateOf(setOf<String>()) }
    var notes by remember { mutableStateOf("") }

    val allConcerns = stringResource(id = R.string.profile_concerns_list).split(",")

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Loaded) {
            val profile = (uiState as ProfileUiState.Loaded).profile
            selectedSkinType = profile.skinType
            selectedSensitivity = profile.sensitivityLevel
            selectedConcerns = profile.concerns.toSet()
            notes = profile.notes ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.profile_title), fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToReminders) {
                        Icon(Icons.Default.Notifications, contentDescription = stringResource(id = R.string.profile_reminders))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        if (uiState is ProfileUiState.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = stringResource(id = R.string.profile_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                SectionTitle(stringResource(id = R.string.profile_skin_type))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SkinType.entries.forEach { type ->
                        SelectableChip(
                            label = type.getDisplayName(context),
                            selected = selectedSkinType == type,
                            onClick = { selectedSkinType = type }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SectionTitle(stringResource(id = R.string.profile_sensitivity))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SensitivityLevel.entries.forEach { level ->
                        SelectableChip(
                            label = level.getDisplayName(context),
                            selected = selectedSensitivity == level,
                            onClick = { selectedSensitivity = level },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SectionTitle(stringResource(id = R.string.profile_concerns))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allConcerns.forEach { concern ->
                        SelectableChip(
                            label = concern,
                            selected = selectedConcerns.contains(concern),
                            onClick = {
                                selectedConcerns = if (selectedConcerns.contains(concern)) {
                                    selectedConcerns - concern
                                } else {
                                    selectedConcerns + concern
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SectionTitle(stringResource(id = R.string.profile_notes))
                AppTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = stringResource(id = R.string.profile_observations),
                    modifier = Modifier.height(120.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (uiState is ProfileUiState.Error) {
                    Text(
                        text = (uiState as ProfileUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                if (uiState is ProfileUiState.Saved) {
                    Text(
                        text = stringResource(id = R.string.profile_saved),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                PrimaryButton(
                    text = if (uiState is ProfileUiState.Saving) stringResource(id = R.string.profile_saving) else stringResource(id = R.string.save),
                    onClick = {
                        viewModel.saveProfile(
                            selectedSkinType,
                            selectedSensitivity,
                            selectedConcerns.toList(),
                            notes
                        )
                    },
                    enabled = uiState !is ProfileUiState.Saving
                )

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SelectableChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    Box(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .then(
                if (!selected) Modifier.border(1.dp, borderColor, RoundedCornerShape(12.dp))
                else Modifier
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}
