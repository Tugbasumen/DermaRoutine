package com.dermaroutine.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.R
import com.dermaroutine.core.designsystem.AppTextField
import com.dermaroutine.core.designsystem.PrimaryButton
import com.dermaroutine.domain.model.RoutineType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoutineScreen(
    onNavigateBack: () -> Unit,
    onRoutineCreated: () -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(RoutineType.Morning) }
    
    val createState by viewModel.createState.collectAsState()

    LaunchedEffect(createState) {
        if (createState is CreateRoutineUiState.Success) {
            onRoutineCreated()
            viewModel.resetCreateState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.routine_new), fontWeight = FontWeight.Bold) },
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
                .padding(24.dp)
                .fillMaxSize()
        ) {
            AppTextField(
                value = name,
                onValueChange = { name = it },
                label = stringResource(id = R.string.routine_name_label),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = stringResource(id = R.string.routine_type_label), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TypeCard(
                    label = stringResource(id = R.string.routine_morning),
                    selected = selectedType == RoutineType.Morning,
                    onClick = { selectedType = RoutineType.Morning },
                    modifier = Modifier.weight(1f)
                )
                TypeCard(
                    label = stringResource(id = R.string.routine_evening),
                    selected = selectedType == RoutineType.Evening,
                    onClick = { selectedType = RoutineType.Evening },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (createState is CreateRoutineUiState.Error) {
                Text(
                    text = (createState as CreateRoutineUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            PrimaryButton(
                text = if (createState is CreateRoutineUiState.Loading) stringResource(id = R.string.loading) else stringResource(id = R.string.routine_create),
                onClick = { viewModel.createRoutine(name, selectedType) },
                enabled = createState !is CreateRoutineUiState.Loading
            )
        }
    }
}

@Composable
fun TypeCard(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val backgroundColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = contentColor, fontWeight = FontWeight.Bold)
    }
}
