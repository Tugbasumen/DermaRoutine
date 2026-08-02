package com.dermaroutine.presentation.progress

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.model.SkinPhoto
import com.dermaroutine.domain.usecase.AddSkinPhotoUseCase
import com.dermaroutine.domain.usecase.GetSkinPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getSkinPhotosUseCase: GetSkinPhotosUseCase,
    private val addSkinPhotoUseCase: AddSkinPhotoUseCase
) : ViewModel() {

    private val _listState = MutableStateFlow<ProgressListUiState>(ProgressListUiState.Loading)
    val listState = _listState.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadUiState>(UploadUiState.Idle)
    val uploadState = _uploadState.asStateFlow()

    fun loadPhotos() {
        viewModelScope.launch {
            _listState.value = ProgressListUiState.Loading
            getSkinPhotosUseCase().onSuccess { photos ->
                if (photos.isEmpty()) _listState.value = ProgressListUiState.Empty
                else _listState.value = ProgressListUiState.Success(photos)
            }.onFailure {
                _listState.value = ProgressListUiState.Error(it.message ?: "Failed to load photos")
            }
        }
    }

    fun uploadPhoto(uri: Uri, rating: Int, notes: String?) {
        viewModelScope.launch {
            _uploadState.value = UploadUiState.Uploading
            val takenAt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            addSkinPhotoUseCase(uri, takenAt, rating, notes).onSuccess {
                _uploadState.value = UploadUiState.Success
                loadPhotos()
            }.onFailure {
                _uploadState.value = UploadUiState.Error(it.message ?: "Failed to upload photo")
            }
        }
    }

    fun resetUploadState() {
        _uploadState.value = UploadUiState.Idle
    }
}

sealed class ProgressListUiState {
    data object Loading : ProgressListUiState()
    data object Empty : ProgressListUiState()
    data class Success(val photos: List<SkinPhoto>) : ProgressListUiState()
    data class Error(val message: String) : ProgressListUiState()
}

sealed class UploadUiState {
    data object Idle : UploadUiState()
    data object Uploading : UploadUiState()
    data object Success : UploadUiState()
    data class Error(val message: String) : UploadUiState()
}
