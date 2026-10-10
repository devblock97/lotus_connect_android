package devblock.tech.lotus_connect_android.feature.chat.domain.repositories

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getMessagesHistory(conversationId: String) : Result<List<MessageEntity>>
    fun observeIncomingMessages(conversationId: String): Flow<MessageEntity>
    fun sendWebSocketMessage(content: String): Boolean
    suspend fun sendMessage(conversationId: String, content: String): Result<MessageEntity>
}