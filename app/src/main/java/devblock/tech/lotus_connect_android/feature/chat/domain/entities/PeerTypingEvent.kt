package devblock.tech.lotus_connect_android.feature.chat.domain.entities

data class PeerTypingEvent(
    val conversationId: String,
    val senderId: String,
    val isTyping: Boolean
)