package com.dermaroutine.presentation.scanner

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.scanner.BarcodeScanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarcodeScannerViewModel @Inject constructor(
    private val barcodeScanner: BarcodeScanner
) : ViewModel() {

    private val _uiState = MutableStateFlow<BarcodeScannerUiState>(BarcodeScannerUiState.Scanning)
    val uiState = _uiState.asStateFlow()

    fun onImageCaptured(imageProxy: ImageProxy) {
        if (_uiState.value is BarcodeScannerUiState.Detected) {
            imageProxy.close()
            return
        }

        viewModelScope.launch {
            val result = barcodeScanner.scan(imageProxy).first()
            if (result != null) {
                _uiState.value = BarcodeScannerUiState.Detected(result)
            }
        }
    }

    fun resetState() {
        _uiState.value = BarcodeScannerUiState.Scanning
    }
}

sealed class BarcodeScannerUiState {
    data object Scanning : BarcodeScannerUiState()
    data class Detected(val barcode: String) : BarcodeScannerUiState()
    data class Error(val message: String) : BarcodeScannerUiState()
}
