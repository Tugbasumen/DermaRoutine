package com.dermaroutine.core.error

import androidx.annotation.StringRes
import com.dermaroutine.R

sealed class AppError(@StringRes val messageRes: Int, val customMessage: String? = null) {
    class Network(message: String? = null) : AppError(R.string.error_network, message)
    class Unauthorized(message: String? = null) : AppError(R.string.auth_welcome, message)
    class Server(message: String? = null) : AppError(R.string.error_unknown, message)
    class NotFound(message: String? = null) : AppError(R.string.error_unknown, message)
    class Validation(message: String) : AppError(R.string.error_unknown, message)
    class Unknown(message: String? = null) : AppError(R.string.error_unknown, message)
}
