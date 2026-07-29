package com.dermaroutine.domain.repository

import com.dermaroutine.domain.model.SkinProfile

interface ProfileRepository {
    suspend fun getProfile(): Result<SkinProfile?>
    suspend fun saveProfile(profile: SkinProfile, isUpdate: Boolean): Result<Unit>
}
