package devblock.tech.lotus_connect_android.feature.contacts.data.models

import com.google.gson.annotations.SerializedName

data class RejectFriendRequest(
    @SerializedName("friendId") val friendId: String
)