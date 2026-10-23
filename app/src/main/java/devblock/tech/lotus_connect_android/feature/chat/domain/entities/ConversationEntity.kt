package devblock.tech.lotus_connect_android.feature.chat.domain.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ConversationEntity(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("isGroup") val isGroup: Boolean,
    @SerializedName("peerId") val peerId: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
) : Serializable
