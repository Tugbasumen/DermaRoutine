package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.User
import com.dermaroutine.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<User?> {
        return repository.getCurrentUser()
    }
}
