package com.dermaroutine.presentation.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaroutine.domain.model.ChatMessage
import com.dermaroutine.domain.usecase.GetCurrentUserUseCase
import com.dermaroutine.domain.usecase.GetChatResponseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val getChatResponseUseCase: GetChatResponseUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping = _isTyping.asStateFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collectLatest { user ->
                val name = user?.displayName ?: ""
                val greeting = if (name.isNotEmpty()) "Merhaba $name! ✨" else "Merhaba!"
                _messages.value = listOf(
                    ChatMessage(
                        text = "$greeting Bugün cilt bakımın hakkında sana nasıl yardımcı olabilirim?",
                        isFromUser = false
                    )
                )
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = ChatMessage(text = text, isFromUser = true)
        _messages.value = _messages.value + userMessage

        viewModelScope.launch {
            _isTyping.value = true
            getChatResponseUseCase(text).onSuccess { response ->
                val aiMessage = ChatMessage(text = response, isFromUser = false)
                _messages.value = _messages.value + aiMessage
            }.onFailure {
                val errorMessage = ChatMessage(
                    text = "Üzgünüm, bir hata oluştu. Lütfen tekrar dene.",
                    isFromUser = false
                )
                _messages.value = _messages.value + errorMessage
            }
            _isTyping.value = false
        }
    }
}
