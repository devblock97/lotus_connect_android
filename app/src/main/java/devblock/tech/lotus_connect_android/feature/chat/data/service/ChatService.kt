package devblock.tech.lotus_connect_android.feature.chat.data.service

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatService {
    @GET("chats")
    suspend fun conversationList(): Response<List<ConversationEntity>>

    @GET("chats/{conversationId}/messages?limit=100")
    suspend fun getMessagesHistory(
        @Path("conversationId") conversationId: String
    ): Response<List<MessageEntity>>

}