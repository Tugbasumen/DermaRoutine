package com.dermaroutine.data.repository

import com.dermaroutine.domain.model.User
import com.dermaroutine.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                Result.success(User(firebaseUser.uid, firebaseUser.email ?: ""))
            } else {
                Result.failure(Exception("Login failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(mapFirebaseException(e))
        }
    }

    override suspend fun register(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                Result.success(User(firebaseUser.uid, firebaseUser.email ?: ""))
            } else {
                Result.failure(Exception("Registration failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(mapFirebaseException(e))
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                Result.success(User(firebaseUser.uid, firebaseUser.email ?: ""))
            } else {
                Result.failure(Exception("Google Sign-In failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            val user = firebaseUser?.let { User(it.uid, it.email ?: "") }
            trySend(user)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    private fun mapFirebaseException(e: Exception): Exception {
        // Basic mapping as per requirements
        return when (e.message?.contains("email address is badly formatted") == true) {
            true -> Exception("Please enter a valid email.")
            else -> e
        }
    }
}
