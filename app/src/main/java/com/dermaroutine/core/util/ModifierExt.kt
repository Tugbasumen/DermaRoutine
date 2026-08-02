package com.dermaroutine.core.util

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.debouncedClickable(
    debounceTime: Long = 500L,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    
    this.clickable(enabled = enabled) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            onClick()
        }
    }
}
