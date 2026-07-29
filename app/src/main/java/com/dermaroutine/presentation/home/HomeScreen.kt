package com.dermaroutine.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.core.designsystem.PrimaryButton
import com.dermaroutine.core.designsystem.SecondaryButton

@Composable
fun HomeScreen(
    onNavigateToRoutine: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToJournal: () -> Unit,
    onNavigateToAssistant: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            HomeHeader(
                greeting = uiState.greeting,
                userName = uiState.userName,
                date = uiState.date
            )
        }

        item {
            RoutineSection(
                morningStatus = uiState.morningRoutineStatus,
                eveningStatus = uiState.eveningRoutineStatus,
                onCreateRoutine = onNavigateToRoutine
            )
        }

        item {
            StreakCard(
                streak = uiState.currentStreak,
                onViewProgress = {} // Placeholder
            )
        }

        item {
            TodaySkinCard(
                hasPhoto = uiState.hasTodayPhoto,
                onAddPhoto = onNavigateToJournal
            )
        }

        item {
            QuickActionsSection(
                onAddProduct = onNavigateToProducts,
                onAddPhoto = onNavigateToJournal,
                onAskAI = onNavigateToAssistant
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HomeHeader(greeting: String, userName: String, date: String) {
    Column {
        Text(
            text = "$greeting${if (userName.isNotEmpty()) ", $userName" else ""} 🌿",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = date,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun RoutineSection(
    morningStatus: String,
    eveningStatus: String,
    onCreateRoutine: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Today's Routine",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RoutineSummaryCard(
                title = "Morning",
                icon = Icons.Default.LightMode,
                status = morningStatus,
                modifier = Modifier.weight(1f),
                onClick = onCreateRoutine
            )
            RoutineSummaryCard(
                title = "Evening",
                icon = Icons.Default.Nightlight,
                status = eveningStatus,
                modifier = Modifier.weight(1f),
                onClick = onCreateRoutine
            )
        }
    }
}

@Composable
fun RoutineSummaryCard(
    title: String,
    icon: ImageVector,
    status: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun StreakCard(streak: Int, onViewProgress: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🔥", fontSize = 32.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Current Streak",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "$streak days",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            IconButton(onClick = onViewProgress) {
                Icon(Icons.Default.ChevronRight, contentDescription = null)
            }
        }
    }
}

@Composable
fun TodaySkinCard(hasPhoto: Boolean, onAddPhoto: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Today's Skin",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (hasPhoto) "Photo added! 🌿" else "Keep track of your skin journey.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = if (hasPhoto) "View Photo" else "Add Today's Photo",
                onClick = onAddPhoto
            )
        }
    }
}

@Composable
fun QuickActionsSection(
    onAddProduct: () -> Unit,
    onAddPhoto: () -> Unit,
    onAskAI: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionItem(
                icon = Icons.Default.Add,
                label = "Product",
                onClick = onAddProduct,
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.CameraAlt,
                label = "Photo",
                onClick = onAddPhoto,
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.SmartToy,
                label = "Ask AI",
                onClick = onAskAI,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun QuickActionItem(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
    }
}
