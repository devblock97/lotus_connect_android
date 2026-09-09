package devblock.tech.lotus_connect_android.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName
import devblock.tech.lotus_connect_android.feature.auth.domain.model.User

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("platform") val platform: String? = "android",
    @SerializedName("deviceToken") val deviceToken: String? = null
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("friendshipStatus") val friendshipStatus: String? = null,
    @SerializedName("friendshipSenderId") val friendshipSenderId: String? = null
) {
    fun toDomain(): User = User(
        id = id,
        username = username,
        email = email,
        fullName = fullName,
        friendshipStatus = friendshipStatus,
        friendshipSenderId = friendshipSenderId
    )
}
