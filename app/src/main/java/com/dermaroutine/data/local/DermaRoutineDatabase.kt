package com.dermaroutine.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dermaroutine.data.local.dao.ProductDao
import com.dermaroutine.data.local.dao.RoutineDao
import com.dermaroutine.data.local.entity.ProductEntity
import com.dermaroutine.data.local.entity.RoutineEntity
import com.dermaroutine.data.local.entity.RoutineItemEntity

@Database(
    entities = [
        ProductEntity::class,
        RoutineEntity::class,
        RoutineItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DermaRoutineDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun routineDao(): RoutineDao
}
