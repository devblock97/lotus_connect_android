package devblock.tech.lotus_connect_android.feature.chat.domain.entities

data class ConversationEntity(
    val id: String,
    val title: String,
    val isGroup: Boolean,
    val peerId: String,
    val createdAt: String,
    val updatedAt: String,
)