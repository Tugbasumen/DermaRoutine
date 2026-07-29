package com.dermaroutine.presentation.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.model.Routine
import com.dermaroutine.domain.model.RoutineDetail
import com.dermaroutine.domain.model.RoutineType
import com.dermaroutine.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val getRoutinesUseCase: GetRoutinesUseCase,
    private val getRoutineDetailUseCase: GetRoutineDetailUseCase,
    private val createRoutineUseCase: CreateRoutineUseCase,
    private val completeRoutineUseCase: CompleteRoutineUseCase,
    private val addItemToRoutineUseCase: AddItemToRoutineUseCase
) : ViewModel() {

    private val _routinesState = MutableStateFlow<RoutineListUiState>(RoutineListUiState.Loading)
    val routinesState = _routinesState.asStateFlow()

    private val _detailState = MutableStateFlow<RoutineDetailUiState>(RoutineDetailUiState.Initial)
    val detailState = _detailState.asStateFlow()

    private val _createState = MutableStateFlow<CreateRoutineUiState>(CreateRoutineUiState.Idle)
    val createState = _createState.asStateFlow()

    fun loadRoutines() {
        viewModelScope.launch {
            _routinesState.value = RoutineListUiState.Loading
            getRoutinesUseCase().onSuccess { routines ->
                if (routines.isEmpty()) _routinesState.value = RoutineListUiState.Empty
                else _routinesState.value = RoutineListUiState.Success(routines)
            }.onFailure {
                _routinesState.value = RoutineListUiState.Error(it.message ?: "Failed to load routines")
            }
        }
    }

    fun loadRoutineDetail(id: Int) {
        viewModelScope.launch {
            _detailState.value = RoutineDetailUiState.Loading
            getRoutineDetailUseCase(id).onSuccess {
                _detailState.value = RoutineDetailUiState.Success(it)
            }.onFailure {
                _detailState.value = RoutineDetailUiState.Error(it.message ?: "Failed to load routine detail")
            }
        }
    }

    fun createRoutine(name: String, type: RoutineType) {
        if (name.isBlank()) {
            _createState.value = CreateRoutineUiState.Error("Name cannot be empty")
            return
        }
        viewModelScope.launch {
            _createState.value = CreateRoutineUiState.Loading
            createRoutineUseCase(name, type).onSuccess {
                _createState.value = CreateRoutineUiState.Success
                loadRoutines()
            }.onFailure {
                _createState.value = CreateRoutineUiState.Error(it.message ?: "Failed to create routine")
            }
        }
    }

    fun completeRoutine(id: Int) {
        viewModelScope.launch {
            completeRoutineUseCase(id).onSuccess {
                loadRoutineDetail(id)
                loadRoutines()
            }
        }
    }

    fun addItemToRoutine(routineId: Int, productId: Int, stepOrder: Int, notes: String?) {
        viewModelScope.launch {
            addItemToRoutineUseCase(routineId, productId, stepOrder, notes).onSuccess {
                loadRoutineDetail(routineId)
            }
        }
    }

    fun resetCreateState() {
        _createState.value = CreateRoutineUiState.Idle
    }
}

sealed class RoutineListUiState {
    data object Loading : RoutineListUiState()
    data object Empty : RoutineListUiState()
    data class Success(val routines: List<Routine>) : RoutineListUiState()
    data class Error(val message: String) : RoutineListUiState()
}

sealed class RoutineDetailUiState {
    data object Initial : RoutineDetailUiState()
    data object Loading : RoutineDetailUiState()
    data class Success(val detail: RoutineDetail) : RoutineDetailUiState()
    data class Error(val message: String) : RoutineDetailUiState()
}

sealed class CreateRoutineUiState {
    data object Idle : CreateRoutineUiState()
    data object Loading : CreateRoutineUiState()
    data object Success : CreateRoutineUiState()
    data class Error(val message: String) : CreateRoutineUiState()
}
