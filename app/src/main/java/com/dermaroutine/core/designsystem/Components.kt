package com.dermaroutine.core.designsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dermaroutine.core.util.debouncedClickable

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            minLines = minLines,
            maxLines = maxLines,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = SecondaryText.copy(alpha = 0.5f),
                focusedLabelColor = Primary,
                cursorColor = Primary
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = Typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    Button(
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > 500L) {
                lastClickTime = currentTime
                onClick()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary,
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            style = Typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    TextButton(
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > 500L) {
                lastClickTime = currentTime
                onClick()
            }
        },
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Primary
        )
    ) {
        Text(
            text = text,
            style = Typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}
