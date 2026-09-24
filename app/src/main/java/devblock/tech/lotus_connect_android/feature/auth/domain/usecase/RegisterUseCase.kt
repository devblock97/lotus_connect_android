package devblock.tech.lotus_connect_android.feature.auth.domain.usecase

import devblock.tech.lotus_connect_android.core.exception.AuthException
import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User
import devblock.tech.lotus_connect_android.feature.auth.domain.repository.AuthRepository

data class RegisterParam(
    val fullName: String,
    val username: String,
    val email: String,
    val password: String
)

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        param: RegisterParam
    ): Result<User> {
        if (param.username.isBlank()) {
            return Result.failure(AuthException.ValidationException("Username", "Username cannot be empty"))
        }
        if (param.fullName.isBlank()) {
            return Result.failure(AuthException.ValidationException("Full name", "Full name cannot be empty"))
        }
        if (param.email.isBlank()) {
            return Result.failure(AuthException.ValidationException("Email", "Email cannot be empty"))
        }
        if (param.password.isBlank()) {
            return Result.failure(AuthException.ValidationException("Password", "Password cannot be empty"))
        }
        return authRepository.register(
            fullName = param.fullName.trim(),
            username = param.username.trim(),
            email = param.email.trim(),
            password = param.password
        )
    }
}