package com.dermaroutine.data.local.dao

import androidx.room.*
import com.dermaroutine.data.local.entity.RoutineEntity
import com.dermaroutine.data.local.entity.RoutineItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines ORDER BY routineType ASC")
    fun getRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutineById(id: Int): RoutineEntity?

    @Query("SELECT * FROM routine_items WHERE routineId = :routineId ORDER BY stepOrder ASC")
    fun getRoutineItems(routineId: Int): Flow<List<RoutineItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutines(routines: List<RoutineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineItems(items: List<RoutineItemEntity>)

    @Query("UPDATE routine_items SET isCompletedToday = :isCompleted WHERE id = :itemId")
    suspend fun updateItemCompletion(itemId: Int, isCompleted: Boolean)

    @Query("DELETE FROM routines")
    suspend fun clearRoutines()

    @Query("DELETE FROM routine_items WHERE routineId = :routineId")
    suspend fun clearRoutineItems(routineId: Int)
}
