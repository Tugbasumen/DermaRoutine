package com.dermaroutine.presentation.scanner.ocr

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.R
import com.dermaroutine.presentation.scanner.CameraPreview
import com.dermaroutine.presentation.scanner.PermissionRequest
import com.dermaroutine.presentation.scanner.ScannerOverlay
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OcrScannerScreen(
    productId: Int,
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: OcrViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(productId) {
        viewModel.setProductId(productId)
    }

    LaunchedEffect(uiState) {
        if (uiState is OcrUiState.Success) {
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.scan_ingredients_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (uiState is OcrUiState.Scanning) Color.Transparent else MaterialTheme.colorScheme.background,
                    titleContentColor = if (uiState is OcrUiState.Scanning) Color.White else MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = if (uiState is OcrUiState.Scanning) Color.White else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is OcrUiState.Scanning -> {
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                        if (cameraPermissionState.status.isGranted) {
                            var captureNextFrame by remember { mutableStateOf(false) }
                            CameraPreview(
                                modifier = Modifier.fillMaxSize(),
                                onImageCaptured = { 
                                    if (captureNextFrame) {
                                        viewModel.onImageCaptured(it)
                                        captureNextFrame = false
                                    } else {
                                        it.close()
                                    }
                                }
                            )
                            ScannerOverlay()
                            
                            Button(
                                onClick = { captureNextFrame = true },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 64.dp)
                                    .size(80.dp),
                                shape = androidx.compose.foundation.shape.CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Icon(Icons.Default.Camera, contentDescription = null, tint = Color.Black, modifier = Modifier.size(40.dp))
                            }
                        } else {
                            PermissionRequest(
                                modifier = Modifier.align(Alignment.Center).padding(24.dp),
                                isPermanentlyDenied = !cameraPermissionState.status.shouldShowRationale,
                                onRequestPermission = { cameraPermissionState.launchPermissionRequest() }
                            )
                        }
                    }
                }
                is OcrUiState.Processing -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                        Text(text = stringResource(id = R.string.scan_analyzing), modifier = Modifier.padding(top = 80.dp))
                    }
                }
                is OcrUiState.ResultReview -> {
                    OcrResultView(
                        rawText = state.rawText,
                        onConfirmed = { viewModel.onTextConfirmed(it) },
                        onRetry = { viewModel.retry() }
                    )
                }
                is OcrUiState.PreviewList -> {
                    IngredientPreviewView(
                        ingredients = state.ingredients,
                        onConfirmed = { viewModel.saveIngredients(it) },
                        onRetry = { viewModel.retry() }
                    )
                }
                is OcrUiState.Saving -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                        Text(text = stringResource(id = R.string.loading), modifier = Modifier.padding(top = 80.dp))
                    }
                }
                is OcrUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(id = R.string.error_unknown), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
                        Text(text = state.message, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = { viewModel.retry() }) {
                            Text(stringResource(id = R.string.try_again))
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun OcrResultView(rawText: String, onConfirmed: (String) -> Unit, onRetry: () -> Unit) {
    var text by remember { mutableStateOf(rawText) }
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(text = stringResource(id = R.string.scan_review_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth().weight(1f),
            label = { Text(stringResource(id = R.string.product_ingredients)) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = onRetry, modifier = Modifier.weight(1f)) {
                Text(stringResource(id = R.string.scan_again))
            }
            Button(onClick = { onConfirmed(text) }, modifier = Modifier.weight(1f)) {
                Text(stringResource(id = R.string.scan_continue))
            }
        }
    }
}

@Composable
fun IngredientPreviewView(
    ingredients: List<com.dermaroutine.domain.model.ProductIngredient>,
    onConfirmed: (List<com.dermaroutine.domain.model.ProductIngredient>) -> Unit,
    onRetry: () -> Unit
) {
    var list by remember { mutableStateOf(ingredients) }
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(text = stringResource(id = R.string.scan_confirmed_title, list.size), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.weight(1f)) {
            items(list.size) { index ->
                val item = list[index]
                ListItem(
                    headlineContent = { Text(item.name) },
                    trailingContent = {
                        IconButton(onClick = { list = list.filterIndexed { i, _ -> i != index } }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = onRetry, modifier = Modifier.weight(1f)) {
                Text(stringResource(id = R.string.cancel))
            }
            Button(onClick = { onConfirmed(list) }, modifier = Modifier.weight(1f)) {
                Text(stringResource(id = R.string.scan_save_all))
            }
        }
    }
}
