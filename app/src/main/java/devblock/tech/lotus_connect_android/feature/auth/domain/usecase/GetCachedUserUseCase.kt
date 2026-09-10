package devblock.tech.lotus_connect_android.feature.auth.domain.usecase

import devblock.tech.lotus_connect_android.feature.auth.domain.model.User
import devblock.tech.lotus_connect_android.feature.auth.domain.repository.AuthRepository

class GetCachedUserUseCase (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User? = authRepository.getCachedUser()
}