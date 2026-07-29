package com.dermaroutine.domain.scanner

import androidx.camera.core.ImageProxy
import kotlinx.coroutines.flow.Flow

interface BarcodeScanner {
    fun scan(imageProxy: ImageProxy): Flow<String?>
}
