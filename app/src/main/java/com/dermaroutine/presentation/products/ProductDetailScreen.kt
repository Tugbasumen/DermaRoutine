package com.dermaroutine.presentation.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.R
import com.dermaroutine.core.designsystem.Primary
import com.dermaroutine.core.util.getDisplayName
import com.dermaroutine.domain.model.ProductAnalysis
import com.dermaroutine.domain.model.ProductCategory
import com.dermaroutine.domain.model.ProductIngredient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToOcr: (Int) -> Unit,
    onNavigateToIngredientAssistant: (String) -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.detailState.collectAsState()
    val analysisState by viewModel.analysisState.collectAsState()
    
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAnalysisSheet by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(productId) {
        viewModel.loadProductDetail(productId)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(id = R.string.product_delete_confirm)) },
            text = { Text(stringResource(id = R.string.product_delete_desc)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProduct(productId)
                        showDeleteDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }

    if (showAnalysisSheet) {
        ModalBottomSheet(
            onDismissRequest = { 
                showAnalysisSheet = false
                viewModel.resetAnalysisState()
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            AnalysisResultContent(analysisState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.product_details), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(productId) }) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(id = R.string.edit))
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete), tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val detailState = state) {
                is ProductDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProductDetailUiState.Success -> {
                    val product = detailState.detail
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item {
                            ProductHeaderSection(
                                product = product,
                                onAnalyzeClick = {
                                    viewModel.analyzeProduct(product.ingredients.map { it.name })
                                    showAnalysisSheet = true
                                }
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.product_ingredients),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                TextButton(onClick = { onNavigateToOcr(productId) }) {
                                    Text(stringResource(id = R.string.product_scan))
                                }
                            }
                        }

                        if (product.ingredients.isEmpty()) {
                            item {
                                Text(
                                    text = stringResource(id = R.string.product_no_ingredients),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        } else {
                            items(product.ingredients) { ingredient ->
                                IngredientRow(
                                    ingredient = ingredient,
                                    onClick = { onNavigateToIngredientAssistant(ingredient.name) }
                                )
                            }
                        }
                        
                        if (!product.notes.isNullOrBlank()) {
                            item {
                                Column {
                                    Text(text = stringResource(id = R.string.product_notes_label), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                    Text(text = product.notes, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
                is ProductDetailUiState.Error -> {
                    Text(
                        text = detailState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(24.dp)
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ProductHeaderSection(
    product: com.dermaroutine.domain.model.ProductDetail,
    onAnalyzeClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🧴", fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = product.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = product.brand, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.category.getDisplayName(context),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Button(
            onClick = onAnalyzeClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(id = R.string.product_analyze_btn), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AnalysisResultContent(state: ProductAnalysisUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is ProductAnalysisUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(id = R.string.product_analyzing))
            }
            is ProductAnalysisUiState.Success -> {
                val analysis = state.analysis
                Text(
                    text = stringResource(id = R.string.product_suitability_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Score Gauge
                SuitabilityGauge(analysis.suitabilityScore)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = analysis.summary,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (analysis.keyBenefits.isNotEmpty()) {
                    AnalysisSection(
                        title = stringResource(id = R.string.product_benefits_title),
                        items = analysis.keyBenefits,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (analysis.potentialRisks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    AnalysisSection(
                        title = stringResource(id = R.string.product_risks_title),
                        items = analysis.potentialRisks,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is ProductAnalysisUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun SuitabilityGauge(score: Int) {
    val color = when {
        score >= 80 -> Primary
        score >= 50 -> Color(0xFFFBC02D)
        else -> Color(0xFFD32F2F)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { score / 100f },
                modifier = Modifier.size(100.dp),
                color = color,
                strokeWidth = 10.dp,
                trackColor = color.copy(alpha = 0.1f)
            )
            Text(
                text = "%$score",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.product_suitability_score),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun AnalysisSection(title: String, items: List<String>, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        Spacer(modifier = Modifier.height(12.dp))
        items.forEach { item ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(text = "•", modifier = Modifier.padding(end = 8.dp), color = color, fontWeight = FontWeight.Bold)
                Text(text = item, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun IngredientRow(ingredient: ProductIngredient, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(text = (ingredient.position + 1).toString(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = ingredient.name, style = MaterialTheme.typography.bodyMedium)
    }
}
