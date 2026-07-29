package com.dermaroutine.domain.repository

import com.dermaroutine.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun logout(): Result<Unit>
    fun getCurrentUser(): Flow<User?>
}
