package com.dermaroutine.presentation.journal

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.model.JournalEntry
import com.dermaroutine.domain.usecase.AddJournalEntryUseCase
import com.dermaroutine.domain.usecase.DeleteJournalEntryUseCase
import com.dermaroutine.domain.usecase.GetJournalEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val getJournalEntriesUseCase: GetJournalEntriesUseCase,
    private val addJournalEntryUseCase: AddJournalEntryUseCase,
    private val deleteJournalEntryUseCase: DeleteJournalEntryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<JournalUiState>(JournalUiState.Loading)
    val uiState = _uiState.asStateFlow()

    // Form State
    var noteContent = mutableStateOf("")
    var selectedMood = mutableStateOf<String?>(null)

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    fun loadJournalEntries() {
        viewModelScope.launch {
            _uiState.value = JournalUiState.Loading
            getJournalEntriesUseCase().onSuccess { entries ->
                if (entries.isEmpty()) _uiState.value = JournalUiState.Empty
                else _uiState.value = JournalUiState.Success(entries)
            }.onFailure {
                _uiState.value = JournalUiState.Error(it.message ?: "Kayıtlar yüklenemedi.")
            }
        }
    }

    fun saveEntry() {
        if (noteContent.value.isBlank()) return

        viewModelScope.launch {
            _isSaving.value = true
            addJournalEntryUseCase(noteContent.value, selectedMood.value).onSuccess {
                noteContent.value = ""
                selectedMood.value = null
                loadJournalEntries()
            }.onFailure {
                // Could handle specific error state for saving
            }
            _isSaving.value = false
        }
    }

    fun deleteEntry(id: Int) {
        viewModelScope.launch {
            deleteJournalEntryUseCase(id).onSuccess {
                loadJournalEntries()
            }
        }
    }
}

sealed class JournalUiState {
    data object Loading : JournalUiState()
    data object Empty : JournalUiState()
    data class Success(val entries: List<JournalEntry>) : JournalUiState()
    data class Error(val message: String) : JournalUiState()
}
