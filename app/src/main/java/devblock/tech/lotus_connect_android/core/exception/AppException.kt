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

fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        is java.net.SocketTimeoutException -> AppException.TimeoutException()
        is java.io.IOException -> AppException.NoInternetException()
        is retrofit2.HttpException -> {
            when (code()) {
                401 -> AppException.UnauthorizedException()
                404 -> AppException.NotFoundException("Resource")
                in 500..599 -> AppException.ServerException(code(), "Server issue, try again later")
                else -> AppException.ServerException(code(), message() ?: "Unknown error")
            }
        }
        else -> AppException.UnknownException(this.message ?: "Something went wrong")
    }
}