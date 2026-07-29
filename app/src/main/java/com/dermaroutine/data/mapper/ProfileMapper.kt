package com.dermaroutine.data.mapper

import com.dermaroutine.data.remote.dto.SkinProfileDto
import com.dermaroutine.domain.model.SensitivityLevel
import com.dermaroutine.domain.model.SkinProfile
import com.dermaroutine.domain.model.SkinType

fun SkinProfileDto.toDomain(): SkinProfile {
    return SkinProfile(
        skinType = SkinType.entries[this.skinType],
        sensitivityLevel = SensitivityLevel.entries[this.sensitivityLevel],
        concerns = this.concerns,
        notes = this.notes
    )
}
