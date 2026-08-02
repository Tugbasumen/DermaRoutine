package com.dermaroutine.domain.usecase

import com.dermaroutine.domain.model.SkinPhoto
import com.dermaroutine.domain.repository.SkinPhotoRepository
import javax.inject.Inject

class GetSkinPhotosUseCase @Inject constructor(
    private val repository: SkinPhotoRepository
) {
    suspend operator fun invoke(): Result<List<SkinPhoto>> {
        return repository.getPhotos()
    }
}
