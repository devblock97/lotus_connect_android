package devblock.tech.lotus_connect_android.core.view_model

import devblock.tech.lotus_connect_android.core.exception.AppException
import devblock.tech.lotus_connect_android.core.exception.AuthException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class BaseViewModelTest {

    private class TestViewModel : BaseViewModel()

    private val viewModel = TestViewModel()

    @Test
    fun testHandleExceptionWithNoInternet() {
        var capturedMessage: String? = null
        var capturedException: AppException? = null

        val appException = viewModel.handleException(
            error = IOException("Network unreachable"),
            defaultMessage = "Something went wrong"
        ) { message, exception ->
            capturedMessage = message
            capturedException = exception
        }

        assertTrue(appException is AppException.NoInternetException)
        assertEquals("No internet connection", capturedMessage)
        assertEquals(appException, capturedException)
    }

    @Test
    fun testHandleExceptionWithTimeout() {
        var capturedMessage: String? = null

        val appException = viewModel.handleException(
            error = SocketTimeoutException("Read timeout"),
            defaultMessage = "Request failed"
        ) { message, _ ->
            capturedMessage = message
        }

        assertTrue(appException is AppException.TimeoutException)
        assertEquals("Request time out", capturedMessage)
    }

    @Test
    fun testHandleExceptionWithAuthException() {
        var capturedMessage: String? = null

        val appException = viewModel.handleException(
            error = AuthException.InvalidCredentialsException("Invalid password provided"),
            defaultMessage = "Auth failed"
        ) { message, _ ->
            capturedMessage = message
        }

        assertTrue(appException is AuthException.InvalidCredentialsException)
        assertEquals("Invalid password provided", capturedMessage)
    }

    @Test
    fun testGetErrorMessage() {
        val ioMessage = viewModel.getErrorMessage(IOException("No connection"))
        assertEquals("No internet connection", ioMessage)

        val timeoutMessage = viewModel.getErrorMessage(SocketTimeoutException())
        assertEquals("Request time out", timeoutMessage)

        val customErrorMessage = viewModel.getErrorMessage(
            Exception("Custom unexpected failure"),
            defaultMessage = "Fallback"
        )
        assertEquals("Custom unexpected failure", customErrorMessage)
    }
}
