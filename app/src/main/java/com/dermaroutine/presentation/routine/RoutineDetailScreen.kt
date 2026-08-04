package com.dermaroutine.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.dermaroutine.core.designsystem.PrimaryButton
import com.dermaroutine.domain.model.RoutineItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailScreen(
    routineId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToAddProduct: () -> Unit,
    selectedProductId: Int? = null,
    onProductHandled: () -> Unit = {},
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val state by viewModel.detailState.collectAsState()

    LaunchedEffect(routineId) {
        viewModel.loadRoutineDetail(routineId)
    }

    LaunchedEffect(selectedProductId) {
        if (selectedProductId != null) {
            viewModel.addItemToRoutine(
                routineId, 
                selectedProductId, 
                (state as? RoutineDetailUiState.Success)?.detail?.items?.size ?: 0,
                null
            )
            onProductHandled()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    if (state is RoutineDetailUiState.Success) {
                        Text((state as RoutineDetailUiState.Success).detail.name, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAddProduct) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.product_scan))
                    }
                    IconButton(onClick = { /* Edit action */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = stringResource(id = R.string.edit))
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val detailState = state) {
                is RoutineDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is RoutineDetailUiState.Success -> {
                    val detail = detailState.detail
                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (detail.items.isEmpty()) {
                                item {
                                    Box(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                                        Text(text = stringResource(id = R.string.routine_no_products), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                    }
                                }
                            } else {
                                itemsIndexed(detail.items) { index, item ->
                                    RoutineItemRow(index + 1, item)
                                }
                            }
                        }
                        
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shadowElevation = 8.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Box(modifier = Modifier.padding(24.dp)) {
                                PrimaryButton(
                                    text = if (detail.isCompletedToday) stringResource(id = R.string.routine_completed_today) else stringResource(id = R.string.routine_complete_btn),
                                    onClick = { viewModel.completeRoutine(detail.id) },
                                    enabled = !detail.isCompletedToday,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                is RoutineDetailUiState.Error -> {
                    Text(text = detailState.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                }
                else -> {}
            }
        }
    }
}

@Composable
fun RoutineItemRow(index: Int, item: RoutineItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(text = index.toString(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.productName, fontWeight = FontWeight.Bold)
            if (!item.productBrand.isNullOrEmpty()) {
                Text(text = item.productBrand, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
        
        if (!item.notes.isNullOrEmpty()) {
            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        }
    }
}
