package devblock.tech.lotus_connect_android.feature.chat.domain.entities

import com.google.gson.annotations.SerializedName

data class MessageEntity(
    val id: String,
    val content: String,
    @SerializedName("sender_id", alternate = ["senderId"]) val senderId: String,
    @SerializedName("created_at", alternate = ["createdAt"]) val createdAt: String,
    @SerializedName("conversation_id", alternate = ["conversationId"]) val conversationId: String? = null,
    @SerializedName("media_url", alternate = ["mediaUrl"]) val mediaUrl: String? = null,
    @SerializedName("message_type", alternate = ["messageType", "type"]) val type: String? = "text", // "TEXT", "IMAGE", "VIDEO"
    @SerializedName("reply_to_id", alternate = ["replyToId"]) val replyToId: String? = null,
    @SerializedName("thumbnail_url", alternate = ["thumbnailUrl"]) val thumbnailUrl: String? = null,
    @SerializedName("file_name", alternate = ["fileName"]) val fileName: String? = null,
    @SerializedName("file_size", alternate = ["fileSize"]) val fileSize: Long? = null,
    @SerializedName("mime_type", alternate = ["mimeType"]) val mimeType: String? = null,
    @SerializedName("duration") val duration: Long? = null,
    @SerializedName("media_items", alternate = ["mediaItems"]) val _mediaItems: List<ChatMediaItemEntity>? = emptyList(),
) {
    val mediaItems: List<ChatMediaItemEntity> get() = _mediaItems ?: emptyList()
}

data class ChatMediaItemEntity(
    @SerializedName("url") val url: String,
    @SerializedName("thumbnail_url", alternate = ["thumbnailUrl"]) val thumbnailUrl: String? = null,
    @SerializedName("file_name", alternate = ["fileName"]) val fileName: String? = null,
    @SerializedName("file_size", alternate = ["fileSize"]) val fileSize: Long? = null,
    @SerializedName("mime_type", alternate = ["mimeType"]) val mimeType: String? = null,
    @SerializedName("duration") val duration: Long? = null,
    @SerializedName("width") val width: Int? = null,
    @SerializedName("height") val height: Int? = null,
)
