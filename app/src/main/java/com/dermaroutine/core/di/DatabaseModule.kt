package com.dermaroutine.core.di

import android.content.Context
import androidx.room.Room
import com.dermaroutine.data.local.DermaRoutineDatabase
import com.dermaroutine.data.local.dao.ProductDao
import com.dermaroutine.data.local.dao.RoutineDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DermaRoutineDatabase {
        return Room.databaseBuilder(
            context,
            DermaRoutineDatabase::class.java,
            "dermaroutine_db"
        ).build()
    }

    @Provides
    fun provideProductDao(database: DermaRoutineDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideRoutineDao(database: DermaRoutineDatabase): RoutineDao {
        return database.routineDao()
    }
}
