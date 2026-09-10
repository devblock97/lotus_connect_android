package devblock.tech.lotus_connect_android.feature.auth.domain.repository

import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.UserDto
import devblock.tech.lotus_connect_android.feature.auth.domain.model.User

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String,
        platform: String? = "android",
        deviceToken: String? = null,
    ): Result<User>

    suspend fun register(
        fullName: String,
        username: String,
        email: String,
        password: String
    ): Result<User>

    suspend fun logout(): Result<Unit>

    suspend fun getCachedUser(): User?
}