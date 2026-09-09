package devblock.tech.lotus_connect_android.feature.auth.domain.repository

import devblock.tech.lotus_connect_android.feature.auth.domain.model.User

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String,
        platform: String? = "android",
        deviceToken: String? = null,
    ): Result<User>

    suspend fun logout(): Result<Unit>

    suspend fun getCachedUser(): User?
}