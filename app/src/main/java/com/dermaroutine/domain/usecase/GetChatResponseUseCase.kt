package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.repository.AssistantRepository
import javax.inject.Inject

class GetChatResponseUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(message: String): Result<String> {
        return repository.getChatResponse(message)
    }
}
