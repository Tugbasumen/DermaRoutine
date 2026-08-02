package com.dermaroutine.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.usecase.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = signInWithGoogleUseCase(idToken)
            result.onSuccess {
                _uiState.value = AuthUiState.Success
            }.onFailure {
                _uiState.value = AuthUiState.Error(it.message ?: "Google Sign-In failed.")
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    fun setError(message: String) {
        _uiState.value = AuthUiState.Error(message)
    }
}

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
