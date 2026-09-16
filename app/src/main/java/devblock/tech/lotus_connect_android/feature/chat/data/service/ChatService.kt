package devblock.tech.lotus_connect_android.feature.chat.data.service

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity
import retrofit2.Response
import retrofit2.http.GET

interface ChatService {
    @GET("chats")
    suspend fun conversationList(): Response<List<ConversationEntity>>
}