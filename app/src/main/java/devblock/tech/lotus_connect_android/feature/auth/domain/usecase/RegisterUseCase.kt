package devblock.tech.lotus_connect_android.feature.auth.domain.usecase

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
        return authRepository.register(
            fullName = param.fullName,
            username = param.username,
            email = param.email,
            password = param.password
        )
    }
}