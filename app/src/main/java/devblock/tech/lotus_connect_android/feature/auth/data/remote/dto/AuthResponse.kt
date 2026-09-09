package devblock.tech.lotus_connect_android.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("user") val user: UserDto,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("success") val isSuccess: Boolean? = null,
    @SerializedName("message") val message: String? = null
)