package devblock.tech.lotus_connect_android.core.exception

sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    data class NoInternetException(val reason: String = "No internet connection")
        : AppException(reason)

    data class TimeoutException(val reason: String = "Request time out")
        : AppException(reason)

    data class ServerException(val code: Int, val reason: String)
        : AppException("Server error ($code): $reason")

    data class UnauthorizedException(val reason: String = "Session expired, please log in again")
        : AppException(reason)

    data class ValidationException(val field: String, val reason: String)
        : AppException("$field: $reason")

    data class NotFoundException(val resource: String)
        : AppException("$resource not found")

    data class UnknownException(val reason: String = "Something went wrong")
        : AppException(reason)
}

fun Throwable.toAppException(defaultMessage: String = "Something went wrong"): AppException {
    return when (this) {
        is AppException -> this
        is java.net.SocketTimeoutException -> AppException.TimeoutException()
        is java.net.UnknownHostException,
        is java.io.IOException -> AppException.NoInternetException()
        is IllegalArgumentException -> {
            val msg = message ?: "Invalid input"
            when {
                msg.contains("email", ignoreCase = true) -> AuthException.InvalidEmailException(msg)
                msg.contains("password", ignoreCase = true) -> AuthException.WeakPasswordException(msg)
                else -> AppException.ValidationException("Input", msg)
            }
        }
        is retrofit2.HttpException -> {
            val rawError = try { response()?.errorBody()?.string() } catch (_: Exception) { null }
            val errorMsg = parseErrorMessage(rawError)
            when (code()) {
                400 -> {
                    if (errorMsg != null && (errorMsg.contains("credential", ignoreCase = true) ||
                            errorMsg.contains("password", ignoreCase = true))) {
                        AuthException.InvalidCredentialsException(errorMsg)
                    } else if (errorMsg != null && (errorMsg.contains("already exists", ignoreCase = true) ||
                            errorMsg.contains("duplicate", ignoreCase = true))) {
                        AuthException.UserAlreadyExistsException(errorMsg)
                    } else {
                        AppException.ValidationException("Request", errorMsg ?: "Invalid request")
                    }
                }
                401 -> AuthException.InvalidCredentialsException(errorMsg ?: "Session expired or invalid credentials")
                403 -> AppException.UnauthorizedException(errorMsg ?: "Access denied")
                404 -> AppException.NotFoundException(errorMsg ?: "Resource")
                409 -> AuthException.UserAlreadyExistsException(errorMsg ?: "Resource already exists")
                in 500..599 -> AppException.ServerException(code(), errorMsg ?: "Server issue, try again later")
                else -> AppException.ServerException(code(), errorMsg ?: message() ?: "Unknown error (${code()})")
            }
        }
        else -> {
            val rawMsg = message
            val parsed = parseErrorMessage(rawMsg) ?: defaultMessage
            when {
                parsed.contains("credential", ignoreCase = true) ||
                parsed.contains("password", ignoreCase = true) ||
                parsed.contains("unauthorized", ignoreCase = true) ->
                    AuthException.InvalidCredentialsException(parsed)

                parsed.contains("already exists", ignoreCase = true) ||
                parsed.contains("duplicate", ignoreCase = true) ||
                parsed.contains("already registered", ignoreCase = true) ->
                    AuthException.UserAlreadyExistsException(parsed)

                parsed.contains("not found", ignoreCase = true) ->
                    AppException.NotFoundException(parsed)

                else -> AppException.UnknownException(parsed)
            }
        }
    }
}

fun AppException.toUserFriendlyMessage(defaultMessage: String = "An unexpected error occurred"): String {
    return when (this) {
        is AuthException.InvalidCredentialsException -> reason
        is AuthException.UserAlreadyExistsException -> reason
        is AuthException.UserNotFoundException -> reason
        is AuthException.SessionExpiredException -> reason
        is AuthException.WeakPasswordException -> reason
        is AuthException.InvalidEmailException -> reason
        is AuthException.EmailNotVerifiedException -> reason
        is AuthException.ValidationException -> reason
        is AuthException.UnknownAuthException -> reason.ifBlank { defaultMessage }
        is AppException.NoInternetException -> reason
        is AppException.TimeoutException -> reason
        is AppException.ServerException -> "Server error ($code): $reason"
        is AppException.UnauthorizedException -> reason
        is AppException.ValidationException -> "$field: $reason"
        is AppException.NotFoundException -> "$resource not found"
        is AppException.UnknownException -> reason.ifBlank { defaultMessage }
    }
}

fun Throwable.toUserFriendlyMessage(defaultMessage: String = "An unexpected error occurred"): String {
    return toAppException(defaultMessage).toUserFriendlyMessage(defaultMessage)
}