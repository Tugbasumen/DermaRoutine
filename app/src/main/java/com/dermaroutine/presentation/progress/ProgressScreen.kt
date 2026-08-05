package com.dermaroutine.presentation.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dermaroutine.R
import com.dermaroutine.domain.model.MoodStat
import com.dermaroutine.domain.model.ProgressStats
import com.dermaroutine.domain.model.SkinPhoto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    onNavigateToAddPhoto: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToCompare: () -> Unit,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val listState by viewModel.listState.collectAsState()
    val statsState by viewModel.statsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.progress_title), fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToCompare) {
                        Icon(Icons.Default.Compare, contentDescription = stringResource(id = R.string.progress_compare))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddPhoto,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = stringResource(id = R.string.progress_add_photo))
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // 1. Statistics Section
                item(span = { GridItemSpan(2) }) {
                    AnalyticsHeader(statsState)
                }

                // 2. Photos Title
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Fotoğraf Günlüğü",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // 3. Photos Grid
                when (val state = listState) {
                    is ProgressListUiState.Loading -> {
                        item(span = { GridItemSpan(2) }) {
                            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is ProgressListUiState.Empty -> {
                        item(span = { GridItemSpan(2) }) {
                            EmptyProgressState(onAddClick = onNavigateToAddPhoto)
                        }
                    }
                    is ProgressListUiState.Success -> {
                        items(state.photos) { photo ->
                            SkinPhotoCard(
                                photo = photo,
                                onClick = { onNavigateToDetail(photo.id) }
                            )
                        }
                    }
                    is ProgressListUiState.Error -> {
                        item(span = { GridItemSpan(2) }) {
                            Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsHeader(statsState: StatsUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        when (statsState) {
            is StatsUiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            is StatsUiState.Error -> Text(text = statsState.message, color = MaterialTheme.colorScheme.error)
            is StatsUiState.Success -> {
                val stats = statsState.stats
                
                // Routine Consistency Cards
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ConsistencyCard(
                        label = "Sabah Rutini",
                        percentage = stats.morningCompletionRate,
                        modifier = Modifier.weight(1f)
                    )
                    ConsistencyCard(
                        label = "Akşam Rutini",
                        percentage = stats.eveningCompletionRate,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Mood Summary Card
                MoodSummaryCard(stats.moodCounts)
            }
        }
    }
}

@Composable
fun ConsistencyCard(label: String, percentage: Double, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "%${percentage.toInt()}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (percentage / 100).toFloat() },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun MoodSummaryCard(moodCounts: List<MoodStat>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "Cilt Günlüğü (Son 30 Gün)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            if (moodCounts.isEmpty()) {
                Text(text = "Henüz günlük girişi yok.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    moodCounts.take(4).forEach { stat ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stat.mood, fontSize = 24.sp)
                            Text(text = "${stat.count} gün", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SkinPhotoCard(photo: SkinPhoto, onClick: () -> Unit) {
    val date = try {
        val dt = LocalDateTime.parse(photo.takenAt, DateTimeFormatter.ISO_DATE_TIME)
        dt.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
    } catch (e: Exception) {
        photo.takenAt
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = photo.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = date, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⭐ ${photo.rating}/5", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun EmptyProgressState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "📸", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.progress_no_photos), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            text = stringResource(id = R.string.progress_no_photos_desc),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddClick) {
            Text(stringResource(id = R.string.progress_add_photo))
        }
    }
}
