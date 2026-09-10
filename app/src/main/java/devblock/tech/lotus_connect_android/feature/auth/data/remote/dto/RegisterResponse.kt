package devblock.tech.lotus_connect_android.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("friendshipStatus") val friendShipStatus: String?,
    @SerializedName("friendshipSenderId") val friendShipSenderId: String?
)