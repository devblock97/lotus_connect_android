package devblock.tech.lotus_connect_android.feature.chat.data.datasources

import devblock.tech.lotus_connect_android.feature.chat.data.service.ChatService
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity

interface ConversationListRemoteDataSource {
    suspend fun getConversationsList(): List<ConversationEntity>
}

class ConversationListRemoteDataSourceImpl(
    val chatService: ChatService
) : ConversationListRemoteDataSource {


    override suspend fun getConversationsList(): List<ConversationEntity> {
        val response = chatService.conversationList()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            println("get conversation list: $body")
            return body
        } else {
            val error = response.errorBody()?.string()
            throw (Exception("Failed to load conversation list with HTTP error: $error"))
        }
    }

}