package devblock.tech.lotus_connect_android.feature.contacts.domain.entities

import com.google.gson.annotations.SerializedName

data class Friend(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("friendshipStatus") val friendshipStatus: String? = null,
    @SerializedName("friendshipSenderId") val friendshipSenderId: String? = null,
) {
    val friendShipStatus: String? get() = friendshipStatus
    val friendShipSenderId: String? get() = friendshipSenderId
}
