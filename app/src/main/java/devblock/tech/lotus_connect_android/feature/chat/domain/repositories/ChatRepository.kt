package devblock.tech.lotus_connect_android.feature.chat.domain.repositories

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity

interface ChatRepository {
    suspend fun getMessagesHistory(conversationId: String) : Result<List<MessageEntity>>
}