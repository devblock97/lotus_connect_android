package devblock.tech.lotus_connect_android.feature.chat.data.datasources

import devblock.tech.lotus_connect_android.feature.chat.data.service.ChatService
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity

import devblock.tech.lotus_connect_android.feature.chat.data.service.SendMessageDto

interface ChatRemoteDataSource {
    suspend fun getMessagesHistory(conversationId: String): List<MessageEntity>
    suspend fun sendMessage(conversationId: String, content: String): MessageEntity
}

class ChatRemoteDataSourceImpl(
    val chatService: ChatService
): ChatRemoteDataSource {

    override suspend fun getMessagesHistory(conversationId: String): List<MessageEntity> {
        val response = chatService.getMessagesHistory(conversationId)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to load messages history with HTTP error: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun sendMessage(conversationId: String, content: String): MessageEntity {
        val response = chatService.sendMessage(conversationId, SendMessageDto(content = content))

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to send message with HTTP error: ${response.errorBody()?.string()}")
        }
    }

}