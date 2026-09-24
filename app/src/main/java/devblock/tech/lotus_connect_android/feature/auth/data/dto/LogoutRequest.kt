package devblock.tech.lotus_connect_android.feature.auth.data.dto

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
    @SerializedName("refreshToken") val refreshToken: String
)