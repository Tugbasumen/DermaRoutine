package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.User
import com.dermaroutine.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> {
        return repository.signInWithGoogle(idToken)
    }
}
