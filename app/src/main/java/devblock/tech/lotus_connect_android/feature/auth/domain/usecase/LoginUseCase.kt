package devblock.tech.lotus_connect_android.feature.auth.domain.usecase

import devblock.tech.lotus_connect_android.feature.auth.domain.model.User
import devblock.tech.lotus_connect_android.feature.auth.domain.repository.AuthRepository

data class LoginParam(
    val email: String,
    val password: String,
    val platform: String? = "android",
    val deviceToken: String? = null,
)

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(params: LoginParam): Result<User> {
        if (params.email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        if (params.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }
        return authRepository.login(
            email = params.email.trim(),
            password = params.password,
            platform = params.platform,
            deviceToken = params.deviceToken
        )
    }
}