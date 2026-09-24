package devblock.tech.lotus_connect_android.feature.auth

import devblock.tech.lotus_connect_android.core.exception.AuthException
import devblock.tech.lotus_connect_android.core.exception.parseErrorMessage
import devblock.tech.lotus_connect_android.core.exception.parseHttpAuthError
import devblock.tech.lotus_connect_android.core.exception.toAuthException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class AuthExceptionTest {

    @Test
    fun testAuthExceptionSubclasses() {
        val invalidCreds = AuthException.InvalidCredentialsException()
        assertEquals("Invalid email or password", invalidCreds.reason)

        val userExists = AuthException.UserAlreadyExistsException()
        assertEquals("An account with this email or username already exists", userExists.reason)

        val notFound = AuthException.UserNotFoundException()
        assertEquals("Account does not exist", notFound.reason)

        val sessionExpired = AuthException.SessionExpiredException()
        assertEquals("Session expired, please sign in again", sessionExpired.reason)

        val weakPassword = AuthException.WeakPasswordException()
        assertEquals("Password does not meet security requirements", weakPassword.reason)

        val invalidEmail = AuthException.InvalidEmailException()
        assertEquals("Invalid email format", invalidEmail.reason)

        val emailNotVerified = AuthException.EmailNotVerifiedException()
        assertEquals("Please verify your email before logging in", emailNotVerified.reason)

        val validation = AuthException.ValidationException("email", "Email cannot be empty")
        assertEquals("email: Email cannot be empty", validation.message)

        val unknownAuth = AuthException.UnknownAuthException()
        assertEquals("Authentication failed", unknownAuth.reason)
    }

    @Test
    fun testParseErrorMessage() {
        val jsonWithMessage = """{"message": "Incorrect email or password"}"""
        assertEquals("Incorrect email or password", parseErrorMessage(jsonWithMessage))

        val jsonWithError = """{"error": "User already exists"}"""
        assertEquals("User already exists", parseErrorMessage(jsonWithError))

        val jsonWithDetail = """{"detail": "Token expired"}"""
        assertEquals("Token expired", parseErrorMessage(jsonWithDetail))

        val plainText = "Internal Server Error"
        assertEquals("Internal Server Error", parseErrorMessage(plainText))

        assertEquals(null, parseErrorMessage(null))
        assertEquals(null, parseErrorMessage("   "))
    }

    @Test
    fun testParseHttpAuthErrorWithJson() {
        val json401 = """{"message": "Invalid password provided"}"""
        val error401 = parseHttpAuthError(401, json401)
        assertTrue(error401 is AuthException.InvalidCredentialsException)
        assertEquals("Invalid password provided", error401.message)

        val json409 = """{"error": "Username is already taken"}"""
        val error409 = parseHttpAuthError(409, json409)
        assertTrue(error409 is AuthException.UserAlreadyExistsException)
        assertEquals("Username is already taken", error409.message)

        val json500 = """{"message": "Database error"}"""
        val error500 = parseHttpAuthError(500, json500)
        assertTrue(error500 is devblock.tech.lotus_connect_android.core.exception.AppException.ServerException)
    }

    @Test
    fun testParseHttpAuthError() {
        val error401 = parseHttpAuthError(401, null)
        assertTrue(error401 is AuthException.InvalidCredentialsException)

        val error404 = parseHttpAuthError(404, null)
        assertTrue(error404 is AuthException.UserNotFoundException)

        val error409 = parseHttpAuthError(409, null)
        assertTrue(error409 is AuthException.UserAlreadyExistsException)
    }

    @Test
    fun testToAuthException() {
        val timeout = SocketTimeoutException("timeout").toAuthException()
        assertTrue(timeout is devblock.tech.lotus_connect_android.core.exception.AppException.TimeoutException)

        val io = IOException("network error").toAuthException()
        assertTrue(io is devblock.tech.lotus_connect_android.core.exception.AppException.NoInternetException)

        val emailValidation = IllegalArgumentException("Email cannot be empty").toAuthException()
        assertTrue(emailValidation is AuthException.InvalidEmailException)

        val passwordValidation = IllegalArgumentException("Password cannot be empty").toAuthException()
        assertTrue(passwordValidation is AuthException.WeakPasswordException)

        val alreadyExistsException = Exception("Username already exists").toAuthException()
        assertTrue(alreadyExistsException is AuthException.UserAlreadyExistsException)

        val credsException = Exception("Invalid credentials").toAuthException()
        assertTrue(credsException is AuthException.InvalidCredentialsException)
    }
}
