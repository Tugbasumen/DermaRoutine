package com.dermaroutine.data.local

import androidx.room.TypeConverter
import com.dermaroutine.domain.model.ProductCategory
import com.dermaroutine.domain.model.RoutineType

class Converters {
    @TypeConverter
    fun fromProductCategory(value: ProductCategory) = value.name

    @TypeConverter
    fun toProductCategory(value: String) = enumValueOf<ProductCategory>(value)

    @TypeConverter
    fun fromRoutineType(value: RoutineType) = value.name

    @TypeConverter
    fun toRoutineType(value: String) = enumValueOf<RoutineType>(value)
}
