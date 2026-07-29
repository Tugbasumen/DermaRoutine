package com.dermaroutine.data.repository

import com.dermaroutine.data.mapper.toDomain
import com.dermaroutine.data.remote.RoutineApiService
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
            Result.failure(e)
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
            Result.failure(e)
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
            Result.failure(e)
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
            Result.failure(e)
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
            Result.failure(e)
        }
    }
}
