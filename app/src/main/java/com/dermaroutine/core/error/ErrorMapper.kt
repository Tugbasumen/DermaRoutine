package com.dermaroutine.core.error

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object ErrorMapper {
    fun map(e: Throwable): AppError {
        return when (e) {
            is IOException -> AppError.Network()
            is SocketTimeoutException -> AppError.Network("Request timed out. Please try again.")
            is HttpException -> {
                when (e.code()) {
                    401 -> AppError.Unauthorized()
                    403 -> AppError.Unauthorized("You don't have permission to perform this action.")
                    404 -> AppError.NotFound()
                    409 -> AppError.Validation("A conflict occurred.")
                    in 500..599 -> AppError.Server()
                    else -> AppError.Unknown()
                }
            }
            else -> AppError.Unknown()
        }
    }
}
