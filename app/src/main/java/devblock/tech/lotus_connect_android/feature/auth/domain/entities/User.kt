package devblock.tech.lotus_connect_android.feature.auth.domain.entities

import com.google.gson.annotations.SerializedName

data class User(
    val id: String,
    val username: String,
    val email: String,
    val fullName: String? = null,
    @SerializedName("friendshipStatus") val friendshipStatus: String? = null,
    @SerializedName("friendshipSenderId") val friendshipSenderId: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
)