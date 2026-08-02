package com.dermaroutine.data.repository

import com.dermaroutine.core.error.ErrorMapper
import com.dermaroutine.data.mapper.toDomain
import com.dermaroutine.data.remote.RoutineApiService
import com.dermaroutine.data.remote.dto.AddRoutineItemRequest
import com.dermaroutine.data.remote.dto.CreateRoutineRequest
import com.dermaroutine.domain.model.*
import com.dermaroutine.domain.repository.RoutineRepository
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val apiService: RoutineApiService
) : RoutineRepository {

    override suspend fun getRoutines(): Result<List<Routine>> {
        return try {
            val response = apiService.getRoutines()
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load routines"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorMapper.map(e).message))
        }
    }

    override suspend fun getRoutine(id: Int): Result<RoutineDetail> {
        return try {
            val response = apiService.getRoutine(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to load routine detail"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorMapper.map(e).message))
        }
    }

    override suspend fun createRoutine(name: String, routineType: RoutineType): Result<Routine> {
        return try {
            val request = CreateRoutineRequest(name, routineType.ordinal)
            val response = apiService.createRoutine(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to create routine"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorMapper.map(e).message))
        }
    }

    override suspend fun completeRoutine(id: Int): Result<Unit> {
        return try {
            val response = apiService.completeRoutine(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to complete routine"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorMapper.map(e).message))
        }
    }

    override suspend fun getStreak(): Result<StreakInfo> {
        return try {
            val response = apiService.getStreak()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to load streak"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorMapper.map(e).message))
        }
    }

    override suspend fun addItemToRoutine(routineId: Int, productId: Int, stepOrder: Int, notes: String?): Result<Unit> {
        return try {
            val request = AddRoutineItemRequest(productId, stepOrder, notes)
            val response = apiService.addItemToRoutine(routineId, request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Failed to add product to routine"))
        } catch (e: Exception) {
            Result.failure(Exception(ErrorMapper.map(e).message))
        }
    }

    override suspend fun removeItemFromRoutine(routineId: Int, itemId: Int): Result<Unit> {
        return try {
            val response = apiService.removeItemFromRoutine(routineId, itemId)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Failed to remove product from routine"))
        } catch (e: Exception) {
            Result.failure(Exception(ErrorMapper.map(e).message))
        }
    }
}
