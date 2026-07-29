package com.dermaroutine.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dermaroutine.presentation.auth.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {}
) {
    // For now, using LoginViewModel just for logout logic if we want to test
    // Better to have a ProfileViewModel but for Phase 3 this is a shortcut
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Profile Placeholder Screen")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Authentication Successful! 🌿")
        }
    }
}
