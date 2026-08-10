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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.R
import com.dermaroutine.core.designsystem.Primary
import com.dermaroutine.presentation.home.components.CircularRoutineProgress
import com.dermaroutine.presentation.home.components.StatCard
import com.dermaroutine.presentation.home.components.WeeklyBarChart
import com.dermaroutine.domain.model.RoutineDetail
import com.dermaroutine.domain.model.RoutineItem
import com.dermaroutine.domain.model.RoutineType
import java.time.LocalTime

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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                HomeHeader(
                    greeting = if (uiState.greetingRes != 0) stringResource(id = uiState.greetingRes) else "",
                    userName = uiState.userName
                )
            }

            // Featured Routine Card (Time-aware selection)
            item {
                val now = LocalTime.now()
                val featuredRoutine = if (now.hour < 15) {
                    uiState.morningRoutine ?: uiState.eveningRoutine
                } else {
                    uiState.eveningRoutine ?: uiState.morningRoutine
                }
                
                FeaturedRoutineCard(
                    routine = featuredRoutine,
                    onToggleItem = { viewModel.toggleItem(it) },
                    onNavigateToRoutines = onNavigateToRoutine
                )
            }

            // Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        label = "Seri",
                        value = "${uiState.currentStreak} gün",
                        subValue = "Seri devam ediyor",
                        icon = "🔥",
                        containerColor = Color(0xFFFFF3E0),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Cilt Skoru",
                        value = "${uiState.skinScore} puan",
                        subValue = "Cilt skoru",
                        icon = "🌿",
                        containerColor = Color(0xFFE8F5E9),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Weekly Progress
            item {
                WeeklyProgressSection(uiState.weeklyActivity)
            }

            // Next Reminder
            if (uiState.nextReminderTitleRes != null && uiState.nextReminderTime != null) {
                item {
                    NextReminderCard(
                        titleRes = uiState.nextReminderTitleRes!!,
                        time = uiState.nextReminderTime!!
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun HomeHeader(greeting: String, userName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "$userName 👋",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            )
        }
        Row {
            Surface(
                onClick = { /* Notification */ },
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                onClick = { /* Profile */ },
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.size(40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(4.dp).clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun FeaturedRoutineCard(
    routine: RoutineDetail?,
    onToggleItem: (Int) -> Unit,
    onNavigateToRoutines: () -> Unit
) {
    val routineTitle = if (routine?.routineType == RoutineType.Morning) 
        stringResource(id = R.string.routine_morning).uppercase() 
    else 
        stringResource(id = R.string.routine_evening).uppercase()
        
    val completedCount = routine?.items?.count { it.isCompletedToday } ?: 0
    val totalCount = routine?.items?.size ?: 0
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToRoutines() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Primary, Primary.copy(alpha = 0.7f))
                    )
                )
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = routineTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (routine != null) "$completedCount/$totalCount tamamlandı" else stringResource(id = R.string.routine_empty),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    CircularRoutineProgress(percentage = progress)
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (routine != null) {
                    routine.items.take(3).forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .clickable { onToggleItem(item.id) }
                        ) {
                            Icon(
                                imageVector = if (item.isCompletedToday) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (item.isCompletedToday) Color.White else Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item.productName,
                                color = if (item.isCompletedToday) Color.White else Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(id = R.string.routine_empty_desc),
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onNavigateToRoutines,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = stringResource(id = R.string.routine_title), color = Primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WeeklyProgressSection(activity: List<Int>) {
    val completedDays = activity.count { it > 0 }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Haftalık İlerleme", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = stringResource(id = R.string.home_weekly_summary, completedDays),
                        style = MaterialTheme.typography.bodySmall,
                        color = Primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(text = stringResource(id = R.string.home_all), style = MaterialTheme.typography.labelMedium, color = Primary)
            }
            Spacer(modifier = Modifier.height(24.dp))
            WeeklyBarChart(
                activity = if (activity.isNotEmpty()) activity else listOf(2, 4, 3, 5, 2, 6, 3),
                barColor = Primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.home_routine_consistency, 18), // Example streak to match "Emma"
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun NextReminderCard(titleRes: Int, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F4))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⏰", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${stringResource(id = titleRes)} hatırlatması",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${stringResource(id = R.string.home_days, 0).replace("0 Gün", "Bugün")}, $time",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
