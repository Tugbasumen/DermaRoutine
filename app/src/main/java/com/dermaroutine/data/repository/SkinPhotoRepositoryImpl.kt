package com.dermaroutine.data.repository

import android.net.Uri
import com.dermaroutine.data.mapper.toDomain
import com.dermaroutine.data.remote.SkinPhotoApiService
import com.dermaroutine.data.remote.dto.CreateSkinPhotoRequest
import com.dermaroutine.data.remote.dto.UpdateSkinPhotoRequest
import com.dermaroutine.data.remote.storage.SkinPhotoStorage
import com.dermaroutine.domain.model.SkinPhoto
import com.dermaroutine.domain.repository.SkinPhotoRepository
import javax.inject.Inject

class SkinPhotoRepositoryImpl @Inject constructor(
    private val apiService: SkinPhotoApiService,
    private val storage: SkinPhotoStorage
) : SkinPhotoRepository {

    override suspend fun getPhotos(): Result<List<SkinPhoto>> {
        return try {
            val response = apiService.getPhotos()
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load photos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPhoto(id: Int): Result<SkinPhoto> {
        return try {
            val response = apiService.getPhoto(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to load photo detail"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadAndSavePhoto(uri: Uri, takenAt: String, rating: Int, notes: String?): Result<SkinPhoto> {
        val uploadResult = storage.uploadPhoto(uri)
        if (uploadResult.isFailure) return Result.failure(uploadResult.exceptionOrNull()!!)

        val imageUrl = uploadResult.getOrThrow()
        return try {
            val request = CreateSkinPhotoRequest(imageUrl, takenAt, rating, notes)
            val response = apiService.createPhoto(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                // Cleanup storage if metadata save fails
                storage.deletePhoto(imageUrl)
                Result.failure(Exception("Failed to save photo metadata"))
            }
        } catch (e: Exception) {
            storage.deletePhoto(imageUrl)
            Result.failure(e)
        }
    }

    override suspend fun updatePhoto(id: Int, takenAt: String, rating: Int, notes: String?): Result<Unit> {
        return try {
            val request = UpdateSkinPhotoRequest(takenAt, rating, notes)
            val response = apiService.updatePhoto(id, request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Failed to update photo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePhoto(id: Int, imageUrl: String): Result<Unit> {
        return try {
            val apiResponse = apiService.deletePhoto(id)
            if (apiResponse.isSuccessful) {
                storage.deletePhoto(imageUrl)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete photo metadata"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
