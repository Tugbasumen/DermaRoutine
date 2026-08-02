package com.dermaroutine.domain.repository

import android.net.Uri
import com.dermaroutine.domain.model.SkinPhoto

interface SkinPhotoRepository {
    suspend fun getPhotos(): Result<List<SkinPhoto>>
    suspend fun getPhoto(id: Int): Result<SkinPhoto>
    suspend fun uploadAndSavePhoto(uri: Uri, takenAt: String, rating: Int, notes: String?): Result<SkinPhoto>
    suspend fun updatePhoto(id: Int, takenAt: String, rating: Int, notes: String?): Result<Unit>
    suspend fun deletePhoto(id: Int, imageUrl: String): Result<Unit>
}
