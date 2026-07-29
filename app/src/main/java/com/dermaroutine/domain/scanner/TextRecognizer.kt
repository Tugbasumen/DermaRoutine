package com.dermaroutine.domain.scanner

import androidx.camera.core.ImageProxy
import kotlinx.coroutines.flow.Flow

interface TextRecognizer {
    fun recognize(imageProxy: ImageProxy): Flow<String?>
}
