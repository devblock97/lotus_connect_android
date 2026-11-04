package devblock.tech.lotus_connect_android.feature.chat.data.service

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class SendMessageDto(
    val content: String
)

interface ChatService {
    @GET("chats")
    suspend fun conversationList(): Response<List<ConversationEntity>>

    @GET("chats/{conversationId}/messages")
    suspend fun getMessagesHistory(
        @Path("conversationId") conversationId: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 25,
    ): Response<List<MessageEntity>>

    @POST("chats/{conversationId}/messages")
    suspend fun sendMessage(
        @Path("conversationId") conversationId: String,
        @Body body: SendMessageDto
    ): Response<MessageEntity>
}