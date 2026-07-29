package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.SkinProfile
import com.dermaroutine.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<SkinProfile?> {
        return repository.getProfile()
    }
}
