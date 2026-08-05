package com.dermaroutine.presentation.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.dermaroutine.domain.model.JournalEntry
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    viewModel: JournalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadJournalEntries()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.journal_title), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                JournalInputSection(
                    noteContent = viewModel.noteContent.value,
                    onNoteChange = { viewModel.noteContent.value = it },
                    selectedMood = viewModel.selectedMood.value,
                    onMoodSelect = { viewModel.selectedMood.value = it },
                    onSave = { viewModel.saveEntry() },
                    isSaving = isSaving
                )
            }

            item {
                Text(
                    text = stringResource(id = R.string.journal_history_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            when (val state = uiState) {
                is JournalUiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is JournalUiState.Empty -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(id = R.string.progress_no_photos_desc), // Reusing similar string
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
                is JournalUiState.Success -> {
                    items(state.entries) { entry ->
                        JournalEntryCard(
                            entry = entry,
                            onDelete = { viewModel.deleteEntry(entry.id) }
                        )
                    }
                }
                is JournalUiState.Error -> {
                    item {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun JournalInputSection(
    noteContent: String,
    onNoteChange: (String) -> Unit,
    selectedMood: String?,
    onMoodSelect: (String) -> Unit,
    onSave: () -> Unit,
    isSaving: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = stringResource(id = R.string.journal_how_is_skin),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            MoodSelector(
                selectedMood = selectedMood,
                onMoodSelect = onMoodSelect
            )

            AppTextField(
                value = noteContent,
                onValueChange = onNoteChange,
                label = stringResource(id = R.string.journal_note_placeholder),
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            PrimaryButton(
                text = if (isSaving) stringResource(id = R.string.loading) else stringResource(id = R.string.save),
                onClick = onSave,
                enabled = noteContent.isNotBlank() && !isSaving
            )
        }
    }
}

@Composable
fun MoodSelector(
    selectedMood: String?,
    onMoodSelect: (String) -> Unit
) {
    val moods = listOf(
        "😊" to stringResource(id = R.string.mood_good),
        "😐" to stringResource(id = R.string.mood_normal),
        "😕" to stringResource(id = R.string.mood_sensitive),
        "😣" to stringResource(id = R.string.mood_bad)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        moods.forEach { (emoji, label) ->
            val isSelected = selectedMood == emoji
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                    .clickable { onMoodSelect(emoji) }
                    .padding(8.dp)
            ) {
                Text(text = emoji, fontSize = 28.sp)
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun JournalEntryCard(entry: JournalEntry, onDelete: () -> Unit) {
    val date = try {
        val dt = ZonedDateTime.parse(entry.createdAt)
        dt.format(DateTimeFormatter.ofPattern("d MMMM, HH:mm"))
    } catch (e: Exception) {
        entry.createdAt
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (entry.mood != null) {
                        Text(text = entry.mood, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(text = date, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = entry.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
