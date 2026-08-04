package com.dermaroutine.core.util

import android.content.Context
import com.dermaroutine.R
import com.dermaroutine.domain.model.ProductCategory
import com.dermaroutine.domain.model.RoutineType
import com.dermaroutine.domain.model.SkinType
import com.dermaroutine.domain.model.SensitivityLevel

fun ProductCategory.getDisplayName(context: Context): String {
    return when (this) {
        ProductCategory.Cleanser -> "Temizleyici"
        ProductCategory.Toner -> "Tonik"
        ProductCategory.Serum -> "Serum"
        ProductCategory.Moisturizer -> "Nemlendirici"
        ProductCategory.Sunscreen -> "Güneş Kremi"
        ProductCategory.Mask -> "Maske"
        ProductCategory.Exfoliant -> "Peeling"
        ProductCategory.EyeCare -> "Göz Bakımı"
        ProductCategory.Treatment -> "Tedavi"
        ProductCategory.Other -> "Diğer"
    }
}

fun SkinType.getDisplayName(context: Context): String {
    return when (this) {
        SkinType.Dry -> "Kuru"
        SkinType.Oily -> "Yağlı"
        SkinType.Combination -> "Karma"
        SkinType.Normal -> "Normal"
        SkinType.NotSure -> "Emin Değilim"
    }
}

fun SensitivityLevel.getDisplayName(context: Context): String {
    return when (this) {
        SensitivityLevel.Low -> "Düşük"
        SensitivityLevel.Medium -> "Orta"
        SensitivityLevel.High -> "Yüksek"
    }
}

fun RoutineType.getDisplayName(context: Context): String {
    return when (this) {
        RoutineType.Morning -> context.getString(R.string.routine_morning)
        RoutineType.Evening -> context.getString(R.string.routine_evening)
    }
}
