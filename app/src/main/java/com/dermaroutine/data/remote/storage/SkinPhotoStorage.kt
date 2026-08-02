package com.dermaroutine.data.remote.storage

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkinPhotoStorage @Inject constructor(
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) {
    suspend fun uploadPhoto(uri: Uri): Result<String> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("User not authenticated"))
            val photoId = UUID.randomUUID().toString()
            val ref = storage.reference.child("skin-photos/$uid/$photoId.jpg")
            
            ref.putFile(uri).await()
            val downloadUrl = ref.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePhoto(imageUrl: String): Result<Unit> {
        return try {
            val ref = storage.getReferenceFromUrl(imageUrl)
            ref.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
