package com.dermaroutine.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.model.*
import com.dermaroutine.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    private val _listState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val listState = _listState.asStateFlow()

    private val _detailState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Initial)
    val detailState = _detailState.asStateFlow()

    private val _saveState = MutableStateFlow<SaveProductUiState>(SaveProductUiState.Idle)
    val saveState = _saveState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null)

    init {
        combine(_searchQuery.debounce(300), _selectedCategory) { query, category ->
            loadProducts(query, category)
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: ProductCategory?) {
        _selectedCategory.value = category
    }

    fun loadProducts(query: String? = null, category: ProductCategory? = null) {
        viewModelScope.launch {
            _listState.value = ProductListUiState.Loading
            getProductsUseCase(query, category).onSuccess { products ->
                if (products.isEmpty()) _listState.value = ProductListUiState.Empty
                else _listState.value = ProductListUiState.Success(products)
            }.onFailure {
                _listState.value = ProductListUiState.Error(it.message ?: "Failed to load products")
            }
        }
    }

    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            _detailState.value = ProductDetailUiState.Loading
            getProductDetailUseCase(id).onSuccess {
                _detailState.value = ProductDetailUiState.Success(it)
            }.onFailure {
                _detailState.value = ProductDetailUiState.Error(it.message ?: "Failed to load product detail")
            }
        }
    }

    fun saveProduct(id: Int?, name: String, brand: String, category: ProductCategory, barcode: String?, notes: String?) {
        viewModelScope.launch {
            _saveState.value = SaveProductUiState.Loading
            val product = Product(id ?: 0, name, brand, category, barcode, null, notes)
            val result = if (id == null) createProductUseCase(product) else updateProductUseCase(id, product)
            
            result.onSuccess {
                _saveState.value = SaveProductUiState.Success
                loadProducts(_searchQuery.value, _selectedCategory.value)
            }.onFailure {
                _saveState.value = SaveProductUiState.Error(it.message ?: "Failed to save product")
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            deleteProductUseCase(id).onSuccess {
                loadProducts(_searchQuery.value, _selectedCategory.value)
            }.onFailure {
                _detailState.value = ProductDetailUiState.Error(it.message ?: "Failed to delete product")
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = SaveProductUiState.Idle
    }
}

sealed class ProductListUiState {
    data object Loading : ProductListUiState()
    data object Empty : ProductListUiState()
    data class Success(val products: List<Product>) : ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
}

sealed class ProductDetailUiState {
    data object Initial : ProductDetailUiState()
    data object Loading : ProductDetailUiState()
    data class Success(val detail: ProductDetail) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}

sealed class SaveProductUiState {
    data object Idle : SaveProductUiState()
    data object Loading : SaveProductUiState()
    data object Success : SaveProductUiState()
    data class Error(val message: String) : SaveProductUiState()
}
