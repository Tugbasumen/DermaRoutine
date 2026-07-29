package com.dermaroutine.presentation.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.core.designsystem.AppTextField
import com.dermaroutine.core.designsystem.PrimaryButton
import com.dermaroutine.domain.model.ProductCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productId: Int? = null,
    scannedBarcode: String? = null,
    onNavigateBack: () -> Unit,
    onNavigateToScanner: () -> Unit,
    onNavigateToProductDetail: (Int) -> Unit,
    onProductSaved: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    val saveState by viewModel.saveState.collectAsState()
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.loadProductDetail(productId)
        } else {
            viewModel.clearForm()
        }
    }

    LaunchedEffect(scannedBarcode) {
        if (scannedBarcode != null) {
            viewModel.onBarcodeScanned(scannedBarcode)
        }
    }

    LaunchedEffect(detailState) {
        if (detailState is ProductDetailUiState.Success) {
            viewModel.setFormFromProduct((detailState as ProductDetailUiState.Success).detail)
        }
    }

    LaunchedEffect(saveState) {
        if (saveState is SaveProductUiState.Success) {
            onProductSaved()
            viewModel.resetSaveState()
        }
    }

    if (saveState is SaveProductUiState.DuplicateFound) {
        val existingProduct = (saveState as SaveProductUiState.DuplicateFound).product
        AlertDialog(
            onDismissRequest = { viewModel.resetSaveState() },
            title = { Text("Product already in library") },
            text = { Text("This barcode belongs to '${existingProduct.name}'. Would you like to view it instead?") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.resetSaveState()
                    onNavigateToProductDetail(existingProduct.id) 
                }) {
                    Text("View Product")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.resetSaveState() }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId == null) "Add Product" else "Edit Product", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTextField(
                value = viewModel.formName.value,
                onValueChange = { viewModel.formName.value = it },
                label = "Product Name*",
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                value = viewModel.formBrand.value,
                onValueChange = { viewModel.formBrand.value = it },
                label = "Brand",
                modifier = Modifier.fillMaxWidth()
            )

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = isMenuExpanded,
                onExpandedChange = { isMenuExpanded = !isMenuExpanded }
            ) {
                OutlinedTextField(
                    value = viewModel.formCategory.value.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category*") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMenuExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                )
                ExposedDropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false }
                ) {
                    ProductCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                viewModel.formCategory.value = category
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            }

            AppTextField(
                value = viewModel.formBarcode.value,
                onValueChange = { viewModel.formBarcode.value = it },
                label = "Barcode (Optional)",
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = onNavigateToScanner) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan Barcode")
                    }
                }
            )

            AppTextField(
                value = viewModel.formNotes.value,
                onValueChange = { viewModel.formNotes.value = it },
                label = "Notes (Optional)",
                modifier = Modifier.height(120.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (saveState is SaveProductUiState.Error) {
                Text(
                    text = (saveState as SaveProductUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            PrimaryButton(
                text = if (saveState is SaveProductUiState.Loading) "Saving..." else "Save Product",
                onClick = { 
                    viewModel.saveProduct(
                        productId, 
                        viewModel.formName.value, 
                        viewModel.formBrand.value, 
                        viewModel.formCategory.value, 
                        viewModel.formBarcode.value.ifBlank { null }, 
                        viewModel.formNotes.value.ifBlank { null }
                    ) 
                },
                enabled = viewModel.formName.value.isNotBlank() && saveState !is SaveProductUiState.Loading
            )
        }
    }
}
