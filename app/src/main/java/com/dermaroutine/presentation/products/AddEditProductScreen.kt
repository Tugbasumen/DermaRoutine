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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.R
import com.dermaroutine.core.designsystem.AppTextField
import com.dermaroutine.core.designsystem.PrimaryButton
import com.dermaroutine.core.util.getDisplayName
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
    val context = androidx.compose.ui.platform.LocalContext.current

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
            title = { Text(stringResource(id = R.string.product_already_in_library_title)) },
            text = { Text(stringResource(id = R.string.product_already_in_library_desc, existingProduct.name)) },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.resetSaveState()
                    onNavigateToProductDetail(existingProduct.id) 
                }) {
                    Text(stringResource(id = R.string.product_view_existing))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.resetSaveState() }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId == null) stringResource(id = R.string.routine_add) else stringResource(id = R.string.edit), fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTextField(
                value = viewModel.formName.value,
                onValueChange = { viewModel.formName.value = it },
                label = stringResource(id = R.string.product_name_label),
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                value = viewModel.formBrand.value,
                onValueChange = { viewModel.formBrand.value = it },
                label = stringResource(id = R.string.product_brand_label),
                modifier = Modifier.fillMaxWidth()
            )

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = isMenuExpanded,
                onExpandedChange = { isMenuExpanded = !isMenuExpanded }
            ) {
                OutlinedTextField(
                    value = viewModel.formCategory.value.getDisplayName(context),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.product_category_label)) },
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
                            text = { Text(category.getDisplayName(context)) },
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
                label = stringResource(id = R.string.product_barcode_label),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = onNavigateToScanner) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = stringResource(id = R.string.product_scan))
                    }
                }
            )

            AppTextField(
                value = viewModel.formNotes.value,
                onValueChange = { viewModel.formNotes.value = it },
                label = stringResource(id = R.string.product_notes_label),
                modifier = Modifier.height(120.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (saveState is SaveProductUiState.Error) {
                val errorMsg = (saveState as SaveProductUiState.Error).message
                // Map known backend errors to localized ones if possible
                val displayError = if (errorMsg.contains("already exists") || errorMsg.contains("in use")) {
                    stringResource(id = R.string.product_conflict)
                } else errorMsg
                
                Text(
                    text = displayError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            PrimaryButton(
                text = if (saveState is SaveProductUiState.Loading) stringResource(id = R.string.loading) else stringResource(id = R.string.product_save),
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
