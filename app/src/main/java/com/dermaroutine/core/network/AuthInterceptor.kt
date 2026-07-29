package com.dermaroutine.core.network

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val user = firebaseAuth.currentUser
        val requestBuilder = chain.request().newBuilder()

        if (user != null) {
            // runBlocking is generally discouraged but Interceptor is synchronous
            // and we need the token before the request can proceed.
            // Alternatively, we could use an Authenticator for 401s.
            val token = runBlocking {
                try {
                    user.getIdToken(false).await().token
                } catch (e: Exception) {
                    null
                }
            }
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
