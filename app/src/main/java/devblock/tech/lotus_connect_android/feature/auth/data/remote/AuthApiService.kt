package devblock.tech.lotus_connect_android.feature.auth.data.remote

import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.AuthResponseDto
import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.LoginRequest
import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.LogoutRequest
import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.RegisterRequest
import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.RegisterResponseDto
import devblock.tech.lotus_connect_android.feature.auth.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponseDto>

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserDto>
}