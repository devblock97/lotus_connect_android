package devblock.tech.lotus_connect_android.feature.auth.data.datasources

import devblock.tech.lotus_connect_android.feature.auth.data.dto.AuthResponseDto
import devblock.tech.lotus_connect_android.feature.auth.data.dto.LoginRequest
import devblock.tech.lotus_connect_android.feature.auth.data.dto.LogoutRequest
import devblock.tech.lotus_connect_android.feature.auth.data.dto.RegisterRequest
import devblock.tech.lotus_connect_android.feature.auth.data.dto.UserDto
import devblock.tech.lotus_connect_android.feature.auth.data.service.AuthApiService

class AuthRemoteDataSource(
    private val apiService: AuthApiService
) {
    suspend fun login(request: LoginRequest): AuthResponseDto {
        val response = apiService.login(request)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Login failed with HTTP ${response.code()}")
        }
    }

    suspend fun logout(refreshToken: String) {
        try {
            apiService.logout(LogoutRequest(refreshToken = refreshToken))
        } catch (e: Exception) {
            println("Remote logout failed: ${e.message}")
        }
    }

    suspend fun register(request: RegisterRequest): UserDto {
        val response = apiService.register(request)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            throw Exception(error ?: "Register failed with HTTP ${response.code()}")
        }
    }

}