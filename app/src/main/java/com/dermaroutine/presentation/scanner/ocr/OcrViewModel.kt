package com.dermaroutine.presentation.scanner.ocr

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.model.ProductIngredient
import com.dermaroutine.domain.scanner.IngredientTextParser
import com.dermaroutine.domain.scanner.TextRecognizer
import com.dermaroutine.domain.usecase.SaveIngredientsBulkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcrViewModel @Inject constructor(
    private val textRecognizer: TextRecognizer,
    private val saveIngredientsBulkUseCase: SaveIngredientsBulkUseCase
) : ViewModel() {

    private val parser = IngredientTextParser()

    private val _uiState = MutableStateFlow<OcrUiState>(OcrUiState.Scanning)
    val uiState = _uiState.asStateFlow()

    private var currentProductId: Int = -1

    fun setProductId(id: Int) {
        currentProductId = id
    }

    fun onImageCaptured(imageProxy: ImageProxy) {
        if (_uiState.value !is OcrUiState.Scanning) {
            imageProxy.close()
            return
        }

        viewModelScope.launch {
            _uiState.value = OcrUiState.Processing
            val text = textRecognizer.recognize(imageProxy).first()
            if (text != null) {
                _uiState.value = OcrUiState.ResultReview(text)
            } else {
                _uiState.value = OcrUiState.Scanning
            }
        }
    }

    fun onTextConfirmed(text: String) {
        val ingredientNames = parser.parse(text)
        val ingredients = ingredientNames.mapIndexed { index, name ->
            ProductIngredient(id = 0, name = name, position = index, description = null)
        }
        _uiState.value = OcrUiState.PreviewList(ingredients)
    }

    fun saveIngredients(ingredients: List<ProductIngredient>) {
        viewModelScope.launch {
            _uiState.value = OcrUiState.Saving
            saveIngredientsBulkUseCase(currentProductId, ingredients).onSuccess {
                _uiState.value = OcrUiState.Success
            }.onFailure {
                _uiState.value = OcrUiState.Error(it.message ?: "Failed to save ingredients")
            }
        }
    }

    fun retry() {
        _uiState.value = OcrUiState.Scanning
    }
}

sealed class OcrUiState {
    data object Scanning : OcrUiState()
    data object Processing : OcrUiState()
    data class ResultReview(val rawText: String) : OcrUiState()
    data class PreviewList(val ingredients: List<ProductIngredient>) : OcrUiState()
    data object Saving : OcrUiState()
    data object Success : OcrUiState()
    data class Error(val message: String) : OcrUiState()
}
