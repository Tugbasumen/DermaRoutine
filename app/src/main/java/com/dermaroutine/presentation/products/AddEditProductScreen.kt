package com.dermaroutine.presentation.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    onNavigateBack: () -> Unit,
    onProductSaved: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ProductCategory.Cleanser) }
    var barcode by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val saveState by viewModel.saveState.collectAsState()
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.loadProductDetail(productId)
        }
    }

    LaunchedEffect(detailState) {
        if (detailState is ProductDetailUiState.Success) {
            val product = (detailState as ProductDetailUiState.Success).detail
            name = product.name
            brand = product.brand
            selectedCategory = product.category
            barcode = product.barcode ?: ""
            notes = product.notes ?: ""
        }
    }

    LaunchedEffect(saveState) {
        if (saveState is SaveProductUiState.Success) {
            onProductSaved()
            viewModel.resetSaveState()
        }
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
                value = name,
                onValueChange = { name = it },
                label = "Product Name*",
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                value = brand,
                onValueChange = { brand = it },
                label = "Brand",
                modifier = Modifier.fillMaxWidth()
            )

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = isMenuExpanded,
                onExpandedChange = { isMenuExpanded = !isMenuExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory.name,
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
                                selectedCategory = category
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            }

            AppTextField(
                value = barcode,
                onValueChange = { barcode = it },
                label = "Barcode (Optional)",
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                value = notes,
                onValueChange = { notes = it },
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
                    viewModel.saveProduct(productId, name, brand, selectedCategory, barcode.ifBlank { null }, notes.ifBlank { null }) 
                },
                enabled = name.isNotBlank() && saveState !is SaveProductUiState.Loading
            )
        }
    }
}
