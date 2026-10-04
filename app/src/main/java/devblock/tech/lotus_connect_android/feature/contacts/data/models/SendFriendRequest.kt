package devblock.tech.lotus_connect_android.feature.contacts.data.models

import com.google.gson.annotations.SerializedName

data class SendFriendRequest(
    @SerializedName("username") val username: String
)