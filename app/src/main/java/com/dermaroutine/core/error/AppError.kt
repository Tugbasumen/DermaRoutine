package com.dermaroutine.core.error

sealed class AppError(val message: String) {
    class Network(message: String = "No internet connection. Please check and try again.") : AppError(message)
    class Unauthorized(message: String = "Session expired. Please login again.") : AppError(message)
    class Server(message: String = "Something went wrong on our end. Please try again later.") : AppError(message)
    class NotFound(message: String = "Resource not found.") : AppError(message)
    class Validation(message: String) : AppError(message)
    class Unknown(message: String = "An unexpected error occurred.") : AppError(message)
}
