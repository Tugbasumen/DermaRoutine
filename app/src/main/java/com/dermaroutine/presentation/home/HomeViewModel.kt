package com.dermaroutine.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateTimeAndGreeting()
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collectLatest { user ->
                _uiState.value = _uiState.value.copy(
                    userName = user?.displayName ?: user?.email?.split("@")?.get(0) ?: ""
                )
            }
        }
    }

    private fun updateTimeAndGreeting() {
        val now = LocalDateTime.now()
        val hour = now.hour
        
        val greeting = when (hour) {
            in 5..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }

        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault())
        val dateString = now.format(dateFormatter)

        _uiState.value = _uiState.value.copy(
            greeting = greeting,
            date = dateString
        )
    }
}
