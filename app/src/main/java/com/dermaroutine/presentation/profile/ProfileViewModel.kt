package com.dermaroutine.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.model.SensitivityLevel
import com.dermaroutine.domain.model.SkinProfile
import com.dermaroutine.domain.model.SkinType
import com.dermaroutine.domain.usecase.GetProfileUseCase
import com.dermaroutine.domain.usecase.SaveProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private var isUpdate = false

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            getProfileUseCase().onSuccess { profile ->
                if (profile != null) {
                    isUpdate = true
                    _uiState.value = ProfileUiState.Loaded(profile)
                } else {
                    isUpdate = false
                    _uiState.value = ProfileUiState.Empty
                }
            }.onFailure {
                _uiState.value = ProfileUiState.Error(it.message ?: "Failed to load profile")
            }
        }
    }

    fun saveProfile(skinType: SkinType, sensitivity: SensitivityLevel, concerns: List<String>, notes: String?) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Saving
            val profile = SkinProfile(skinType, sensitivity, concerns, notes)
            saveProfileUseCase(profile, isUpdate).onSuccess {
                _uiState.value = ProfileUiState.Saved
                isUpdate = true
            }.onFailure {
                _uiState.value = ProfileUiState.Error(it.message ?: "Failed to save profile")
            }
        }
    }
}

sealed class ProfileUiState {
    data object Initial : ProfileUiState()
    data object Loading : ProfileUiState()
    data object Empty : ProfileUiState()
    data class Loaded(val profile: SkinProfile) : ProfileUiState()
    data object Saving : ProfileUiState()
    data object Saved : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
