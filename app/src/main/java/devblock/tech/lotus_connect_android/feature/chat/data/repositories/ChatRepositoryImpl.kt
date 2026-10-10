package devblock.tech.lotus_connect_android.feature.chat.data.repositories

import devblock.tech.lotus_connect_android.feature.auth.data.datasources.AuthLocalDataSource
import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ChatRemoteDataSource
import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ChatWebSocketDataSource
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl(
    private val remoteDataSource: ChatRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val chatWebSocketDataSource: ChatWebSocketDataSource
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

    override fun observeIncomingMessages(conversationId: String): Flow<MessageEntity> {
        val token = authLocalDataSource.getAccessToken()
        return chatWebSocketDataSource.connect(conversationId, token)
    }

    override fun sendWebSocketMessage(content: String): Boolean {
        return chatWebSocketDataSource.sendMessage(content)
    }

    override suspend fun sendMessage(conversationId: String, content: String): Result<MessageEntity> {
        val wsPayload = com.google.gson.Gson().toJson(
            mapOf(
                "event" to "chat:message",
                "payload" to mapOf(
                    "conversationId" to conversationId,
                    "content" to content
                )
            )
        )

        val sentViaWs = chatWebSocketDataSource.sendMessage(wsPayload)
        if (sentViaWs) {
            val nowIso = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US).apply {
                timeZone = java.util.TimeZone.getTimeZone("UTC")
            }.format(java.util.Date())

            return Result.success(
                MessageEntity(
                    id = "temp_${System.currentTimeMillis()}",
                    content = content,
                    senderId = authLocalDataSource.getCachedUser()?.id ?: "",
                    createdAt = nowIso,
                    conversationId = conversationId
                )
            )
        }

        // Fallback to REST API if WebSocket connection is not yet ready or temporarily offline
        return runCatching {
            remoteDataSource.sendMessage(conversationId, content)
        }
    }
}