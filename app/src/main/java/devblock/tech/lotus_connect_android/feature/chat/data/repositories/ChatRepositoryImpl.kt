package devblock.tech.lotus_connect_android.feature.chat.data.repositories

import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ChatRemoteDataSource
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ChatRepository

class ChatRepositoryImpl(
    private val remoteDataSource: ChatRemoteDataSource
): ChatRepository {

    override
    suspend fun getMessagesHistory(conversationId: String): Result<List<MessageEntity>> = runCatching {
        return try {
            val response = remoteDataSource.getMessagesHistory(conversationId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}