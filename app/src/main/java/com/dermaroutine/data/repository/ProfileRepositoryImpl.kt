package com.dermaroutine.data.repository

import com.dermaroutine.data.mapper.toDomain
import com.dermaroutine.data.remote.ProfileApiService
import com.dermaroutine.data.remote.dto.CreateProfileRequest
import com.dermaroutine.data.remote.dto.UpdateProfileRequest
import com.dermaroutine.domain.model.SkinProfile
import com.dermaroutine.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ProfileApiService
) : ProfileRepository {

    override suspend fun getProfile(): Result<SkinProfile?> {
        return try {
            val response = apiService.getProfile()
            if (response.isSuccessful) {
                Result.success(response.body()?.toDomain())
            } else if (response.code() == 404) {
                Result.success(null)
            } else {
                Result.failure(Exception("Failed to fetch profile: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveProfile(profile: SkinProfile, isUpdate: Boolean): Result<Unit> {
        return try {
            if (isUpdate) {
                val request = UpdateProfileRequest(
                    skinType = profile.skinType.ordinal,
                    sensitivityLevel = profile.sensitivityLevel.ordinal,
                    concerns = profile.concerns,
                    notes = profile.notes
                )
                val response = apiService.updateProfile(request)
                if (response.isSuccessful) Result.success(Unit)
                else Result.failure(Exception("Failed to update profile"))
            } else {
                val request = CreateProfileRequest(
                    skinType = profile.skinType.ordinal,
                    sensitivityLevel = profile.sensitivityLevel.ordinal,
                    concerns = profile.concerns,
                    notes = profile.notes
                )
                val response = apiService.createProfile(request)
                if (response.isSuccessful) Result.success(Unit)
                else Result.failure(Exception("Failed to create profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
