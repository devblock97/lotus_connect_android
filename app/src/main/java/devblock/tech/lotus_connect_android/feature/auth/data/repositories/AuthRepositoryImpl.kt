package devblock.tech.lotus_connect_android.feature.auth.data.repositories

import devblock.tech.lotus_connect_android.feature.auth.data.local.AuthLocalDataSource
import devblock.tech.lotus_connect_android.feature.auth.data.remote.AuthRemoteDataSource
import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.LoginRequest
import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.RegisterRequest
import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.UserDto
import devblock.tech.lotus_connect_android.feature.auth.domain.model.User
import devblock.tech.lotus_connect_android.feature.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String,
        platform: String?,
        deviceToken: String?
    ): Result<User> = runCatching {
        val response = remoteDataSource.login(
            LoginRequest(
                email = email,
                password = password,
                platform = platform,
                deviceToken = deviceToken
            )
        )

        val domainUser = response.user.toDomain()
        localDataSource.saveSession(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            user = domainUser
        )
        domainUser
    }

    override suspend fun register(
        fullName: String,
        username: String,
        email: String,
        password: String
    ): Result<User> = runCatching {
        val response = remoteDataSource.register(
            RegisterRequest(
                username = username,
                fullName = fullName,
                email = email,
                password = password
            )
        )

        val domainUser = response.toDomain()
        domainUser
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val refreshToken = localDataSource.getRefreshToken()

        if (!refreshToken.isNullOrEmpty()) {
            try {
                remoteDataSource.logout(refreshToken)
            } catch (e: Exception) {

            }
        }
        localDataSource.clearSession()
    }

    override suspend fun getCachedUser(): User? {
        return localDataSource.getCachedUser()
    }

}