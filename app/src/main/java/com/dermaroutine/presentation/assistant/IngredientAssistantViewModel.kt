package com.dermaroutine.presentation.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.model.IngredientExplanation
import com.dermaroutine.domain.usecase.GetIngredientExplanationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientAssistantViewModel @Inject constructor(
    private val getIngredientExplanationUseCase: GetIngredientExplanationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AssistantUiState>(AssistantUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun loadExplanation(ingredientName: String) {
        if (ingredientName.isBlank()) {
            _uiState.value = AssistantUiState.Empty
            return
        }

        viewModelScope.launch {
            _uiState.value = AssistantUiState.Loading
            getIngredientExplanationUseCase(ingredientName).onSuccess {
                _uiState.value = AssistantUiState.Success(it)
            }.onFailure {
                _uiState.value = AssistantUiState.Error(it.message ?: "Could not load AI information.")
            }
        }
    }
}

sealed class AssistantUiState {
    data object Initial : AssistantUiState()
    data object Loading : AssistantUiState()
    data object Empty : AssistantUiState()
    data class Success(val explanation: IngredientExplanation) : AssistantUiState()
    data class Error(val message: String) : AssistantUiState()
}
