package devblock.tech.lotus_connect_android.feature.contacts.data.models

import com.google.gson.annotations.SerializedName

data class AcceptFriendRequest(
    @SerializedName("friendId") val friendId: String
)