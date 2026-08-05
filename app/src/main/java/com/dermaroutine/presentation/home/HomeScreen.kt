package com.dermaroutine.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.R
import com.dermaroutine.core.designsystem.PrimaryButton
import com.dermaroutine.core.designsystem.SecondaryButton
import com.dermaroutine.domain.model.RoutineDetail
import com.dermaroutine.domain.model.RoutineItem
import com.dermaroutine.domain.model.RoutineType

@Composable
fun HomeScreen(
    onNavigateToRoutine: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToJournal: () -> Unit,
    onNavigateToAssistant: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

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
                greeting = if (uiState.greetingRes != 0) stringResource(id = uiState.greetingRes) else "",
                userName = uiState.userName,
                date = uiState.date
            )
        }

        item {
            RoutineSection(
                morningRoutine = uiState.morningRoutine,
                eveningRoutine = uiState.eveningRoutine,
                onToggleItem = { viewModel.toggleItem(it) },
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$greeting${if (userName.isNotEmpty()) ", $userName" else ""}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier.size(32.dp).clip(CircleShape)
            )
        }
        Text(
            text = date,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun RoutineSection(
    morningRoutine: RoutineDetail?,
    eveningRoutine: RoutineDetail?,
    onToggleItem: (Int) -> Unit,
    onCreateRoutine: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(id = R.string.home_today_routine),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        
        RoutineChecklistCard(
            routine = morningRoutine,
            icon = Icons.Default.LightMode,
            title = stringResource(id = R.string.routine_morning),
            iconColor = Color(0xFFFFB300),
            onToggleItem = onToggleItem,
            onClick = onCreateRoutine
        )

        RoutineChecklistCard(
            routine = eveningRoutine,
            icon = Icons.Default.Nightlight,
            title = stringResource(id = R.string.routine_evening),
            iconColor = Color(0xFF5C6BC0),
            onToggleItem = onToggleItem,
            onClick = onCreateRoutine
        )
    }
}

@Composable
fun RoutineChecklistCard(
    routine: RoutineDetail?,
    icon: ImageVector,
    title: String,
    iconColor: Color,
    onToggleItem: (Int) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                
                if (routine != null) {
                    val completedCount = routine.items.count { it.isCompletedToday }
                    Text(
                        text = "$completedCount / ${routine.items.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (routine == null) {
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(text = stringResource(id = R.string.routine_add), onClick = onClick)
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                routine.items.forEach { item ->
                    ChecklistItem(
                        item = item,
                        onToggle = { onToggleItem(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ChecklistItem(item: RoutineItem, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = if (item.isCompletedToday) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked
        val color = if (item.isCompletedToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = item.productName,
            style = MaterialTheme.typography.bodyMedium,
            color = if (item.isCompletedToday) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
        )
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
                        text = stringResource(id = R.string.home_current_streak),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = stringResource(id = R.string.home_days, streak),
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
                text = stringResource(id = R.string.home_today_skin),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (hasPhoto) stringResource(id = R.string.home_photo_added) else stringResource(id = R.string.home_keep_track),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = if (hasPhoto) stringResource(id = R.string.home_view_photo) else stringResource(id = R.string.home_add_photo),
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
            text = stringResource(id = R.string.home_quick_actions),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionItem(
                icon = Icons.Default.Add,
                label = stringResource(id = R.string.home_action_product),
                onClick = onAddProduct,
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.CameraAlt,
                label = stringResource(id = R.string.home_action_photo),
                onClick = onAddPhoto,
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.SmartToy,
                label = stringResource(id = R.string.home_action_ai),
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
