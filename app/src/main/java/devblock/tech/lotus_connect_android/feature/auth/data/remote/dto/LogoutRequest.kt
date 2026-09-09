package devblock.tech.lotus_connect_android.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
    @SerializedName("refreshToken") val refreshToken: String
)