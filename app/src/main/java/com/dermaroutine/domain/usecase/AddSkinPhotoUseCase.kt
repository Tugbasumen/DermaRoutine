package com.dermaroutine.domain.usecase

import android.net.Uri
import com.dermaroutine.domain.model.SkinPhoto
import com.dermaroutine.domain.repository.SkinPhotoRepository
import javax.inject.Inject

class AddSkinPhotoUseCase @Inject constructor(
    private val repository: SkinPhotoRepository
) {
    suspend operator fun invoke(uri: Uri, takenAt: String, rating: Int, notes: String?): Result<SkinPhoto> {
        return repository.uploadAndSavePhoto(uri, takenAt, rating, notes)
    }
}
