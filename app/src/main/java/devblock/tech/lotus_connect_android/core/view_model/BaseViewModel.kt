package devblock.tech.lotus_connect_android.core.view_model

import androidx.lifecycle.ViewModel
import devblock.tech.lotus_connect_android.core.exception.AppException
import devblock.tech.lotus_connect_android.core.exception.toAppException
import devblock.tech.lotus_connect_android.core.exception.toUserFriendlyMessage

/**
 * Base ViewModel class that provides reusable exception handling and error message resolution
 * for all ViewModels across the application.
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * Converts any [Throwable] into an [AppException], resolves a user-friendly message,
     * and optionally triggers the [onError] callback with both the message and exception.
     *
     * @param error The thrown exception or error.
     * @param defaultMessage Fallback message if no specific message can be extracted.
     * @param onError Optional callback invoked with the user-friendly message and typed [AppException].
     * @return The parsed [AppException].
     */
    open fun handleException(
        error: Throwable,
        defaultMessage: String = "An unexpected error occurred",
        onError: ((message: String, appException: AppException) -> Unit)? = null
    ): AppException {
        val appException = error.toAppException(defaultMessage)
        val userFriendlyMessage = appException.toUserFriendlyMessage(defaultMessage)
        onError?.invoke(userFriendlyMessage, appException)
        return appException
    }

    /**
     * Returns a user-friendly error message string from any [Throwable].
     *
     * @param error The thrown exception or error.
     * @param defaultMessage Fallback message if no specific message can be extracted.
     * @return The user-friendly error string.
     */
    fun getErrorMessage(
        error: Throwable,
        defaultMessage: String = "An unexpected error occurred"
    ): String {
        return error.toAppException(defaultMessage).toUserFriendlyMessage(defaultMessage)
    }
}
