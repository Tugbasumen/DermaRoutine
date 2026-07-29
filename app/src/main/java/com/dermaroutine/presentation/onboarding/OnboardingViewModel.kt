package com.dermaroutine.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.core.util.OnboardingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val dataStore: OnboardingDataStore
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            dataStore.saveOnboardingCompleted(true)
        }
    }
}
