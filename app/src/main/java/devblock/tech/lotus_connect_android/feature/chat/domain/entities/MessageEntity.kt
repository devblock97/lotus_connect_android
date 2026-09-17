package devblock.tech.lotus_connect_android.feature.chat.domain.entities

import com.google.gson.annotations.SerializedName

data class MessageEntity(
    val id: String,
    val content: String,
    @SerializedName("sender_id") val senderId: String,
    @SerializedName("created_at") val createdAt: String,
)
