package devblock.tech.lotus_connect_android.feature.chat.data.datasources

import devblock.tech.lotus_connect_android.feature.chat.data.service.ChatService
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity

interface ChatRemoteDataSource {
    suspend fun getMessagesHistory(conversationId: String): List<MessageEntity>
}

class ChatRemoteDataSourceImpl(
    val chatService: ChatService
): ChatRemoteDataSource {

    override suspend fun getMessagesHistory(conversationId: String): List<MessageEntity> {
        val response = chatService.getMessagesHistory(conversationId)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to load messages history with HTTP error: ${response.errorBody()}")
        }
    }

}