package com.dermaroutine.data.mapper

import com.dermaroutine.data.remote.dto.*
import com.dermaroutine.domain.model.*

fun RoutineDto.toDomain(): Routine {
    return Routine(
        id = id,
        name = name,
        routineType = RoutineType.entries[routineType],
        isActive = isActive,
        itemCount = itemCount,
        isCompletedToday = isCompletedToday
    )
}

fun RoutineDetailDto.toDomain(): RoutineDetail {
    return RoutineDetail(
        id = id,
        name = name,
        routineType = RoutineType.entries[routineType],
        isActive = isActive,
        items = items.map { it.toDomain() },
        isCompletedToday = isCompletedToday
    )
}

fun RoutineItemDto.toDomain(): RoutineItem {
    return RoutineItem(
        id = id,
        productId = productId,
        productName = productName,
        productBrand = productBrand,
        productImageUrl = productImageUrl,
        stepOrder = stepOrder,
        notes = notes
    )
}

fun StreakDto.toDomain(): StreakInfo {
    return StreakInfo(
        currentStreak = currentStreak,
        todayCompleted = todayCompleted
    )
}
