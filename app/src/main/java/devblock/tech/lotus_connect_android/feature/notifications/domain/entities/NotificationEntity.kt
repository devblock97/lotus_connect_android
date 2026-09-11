package devblock.tech.lotus_connect_android.feature.notifications.domain.entities

import com.google.gson.annotations.SerializedName


data class NotificationEntity(
    val id: String,
    val title: String,
    val body: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_read") val isRead: Boolean,
    val data: NotificationData
)

data class NotificationData(
    val callId: String?,
    val conversationId: String?,
    val callerId: String?,
    val messageId: String?,
    val type: String?,
    val senderId: String?
)