package com.dermaroutine.data.mapper

import com.dermaroutine.data.remote.dto.SkinPhotoDto
import com.dermaroutine.domain.model.SkinPhoto

fun SkinPhotoDto.toDomain(): SkinPhoto {
    return SkinPhoto(
        id = id,
        imageUrl = imageUrl,
        takenAt = takenAt,
        rating = rating,
        notes = notes
    )
}
