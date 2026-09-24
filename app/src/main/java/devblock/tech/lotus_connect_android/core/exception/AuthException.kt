package devblock.tech.lotus_connect_android.core.exception

import com.google.gson.JsonParser

sealed class AuthException(
    message: String,
    cause: Throwable? = null
) : AppException(message, cause) {

    data class InvalidCredentialsException(
        val reason: String = "Invalid email or password"
    ) : AuthException(reason)

    data class UserAlreadyExistsException(
        val reason: String = "An account with this email or username already exists"
    ) : AuthException(reason)

    data class UserNotFoundException(
        val reason: String = "Account does not exist"
    ) : AuthException(reason)

    data class SessionExpiredException(
        val reason: String = "Session expired, please sign in again"
    ) : AuthException(reason)

    data class WeakPasswordException(
        val reason: String = "Password does not meet security requirements"
    ) : AuthException(reason)

    data class InvalidEmailException(
        val reason: String = "Invalid email format"
    ) : AuthException(reason)

    data class EmailNotVerifiedException(
        val reason: String = "Please verify your email before logging in"
    ) : AuthException(reason)

    data class ValidationException(
        val field: String,
        val reason: String
    ) : AuthException("$field: $reason")

    data class UnknownAuthException(
        val reason: String = "Authentication failed"
    ) : AuthException(reason)
}

fun parseErrorMessage(rawError: String?): String? {
    if (rawError.isNullOrBlank()) return null
    return try {
        val jsonElement = JsonParser.parseString(rawError)
        if (jsonElement.isJsonObject) {
            val jsonObject = jsonElement.asJsonObject
            when {
                jsonObject.has("message") && !jsonObject.get("message").isJsonNull
                    -> jsonObject.get("message").asString
                jsonObject.has("error") && !jsonObject.get("error").isJsonNull
                    -> jsonObject.get("error").asString
                jsonObject.has("detail") && !jsonObject.get("detail").isJsonNull
                    -> jsonObject.get("detail").asString
                jsonObject.has("msg") && !jsonObject.get("msg").isJsonNull
                    -> jsonObject.get("msg").asString
                else -> rawError
            }
        } else {
            rawError
        }
    } catch (_: Exception) {
        rawError
    }
}

fun parseHttpAuthError(code: Int, rawBody: String?): AppException {
    val parsedMsg = parseErrorMessage(rawBody)
    return when (code) {
        400 -> {
            if (parsedMsg != null && (parsedMsg.contains("credential", ignoreCase = true) ||
                    parsedMsg.contains("password", ignoreCase = true))) {
                AuthException.InvalidCredentialsException(parsedMsg)
            } else if (parsedMsg != null && (parsedMsg.contains("already exists", ignoreCase = true) ||
                    parsedMsg.contains("duplicate", ignoreCase = true))) {
                AuthException.UserAlreadyExistsException(parsedMsg)
            } else {
                AuthException.ValidationException("request", parsedMsg ?: "Invalid request")
            }
        }
        401 -> AuthException.InvalidCredentialsException(parsedMsg ?: "Invalid email or password")
        403 -> AuthException.InvalidCredentialsException(parsedMsg ?: "Access denied")
        404 -> AuthException.UserNotFoundException(parsedMsg ?: "Account does not exist")
        409 -> AuthException.UserAlreadyExistsException(parsedMsg ?: "An account with this email or username already exists")
        in 500..599 -> AppException.ServerException(code, parsedMsg ?: "Server issue, try again later")
        else -> AppException.ServerException(code, parsedMsg ?: "Error occurred with HTTP $code")
    }
}

fun Throwable.toAuthException(defaultMessage: String = "Authentication failed"): AppException =
    toAppException(defaultMessage)

