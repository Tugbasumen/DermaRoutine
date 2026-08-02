package com.dermaroutine.presentation.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dermaroutine.domain.model.SkinPhoto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparePhotosScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val state by viewModel.listState.collectAsState()
    
    var beforePhoto by remember { mutableStateOf<SkinPhoto?>(null) }
    var afterPhoto by remember { mutableStateOf<SkinPhoto?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compare Progress", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Comparison View
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ComparisonHalf("Before", beforePhoto, Modifier.weight(1f))
                ComparisonHalf("After", afterPhoto, Modifier.weight(1f))
            }

            Text(text = "Select photos from your journey", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            when (val listState = state) {
                is ProgressListUiState.Success -> {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(listState.photos) { photo ->
                            val isSelected = beforePhoto?.id == photo.id || afterPhoto?.id == photo.id
                            
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .then(if (isSelected) Modifier.background(MaterialTheme.colorScheme.primary) else Modifier)
                                    .clickable {
                                        if (beforePhoto == null) beforePhoto = photo
                                        else if (afterPhoto == null && photo.id != beforePhoto?.id) afterPhoto = photo
                                        else if (photo.id == beforePhoto?.id) beforePhoto = null
                                        else if (photo.id == afterPhoto?.id) afterPhoto = null
                                    }
                                    .padding(if (isSelected) 4.dp else 0.dp)
                            ) {
                                AsyncImage(
                                    model = photo.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                else -> {
                    // Handled by ProgressScreen mainly, keeping it simple
                }
            }

            if (beforePhoto != null && afterPhoto != null) {
                Button(
                    onClick = { /* Could expand view */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Full Comparison")
                }
            }
        }
    }
}

@Composable
fun ComparisonHalf(label: String, photo: SkinPhoto?, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (photo != null) {
                AsyncImage(
                    model = photo.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Date overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val date = try {
                        val dt = LocalDateTime.parse(photo.takenAt, DateTimeFormatter.ISO_DATE_TIME)
                        dt.format(DateTimeFormatter.ofPattern("MMM d"))
                    } catch (e: Exception) { "" }
                    Text(text = date, color = Color.White, fontSize = 10.sp)
                }
            } else {
                Text(text = "Select", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
