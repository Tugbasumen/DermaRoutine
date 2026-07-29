package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.SkinProfile
import com.dermaroutine.domain.repository.ProfileRepository
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: SkinProfile, isUpdate: Boolean): Result<Unit> {
        return repository.saveProfile(profile, isUpdate)
    }
}
