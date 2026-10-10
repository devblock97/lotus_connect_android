package devblock.tech.lotus_connect_android.feature.chat.domain.usecase

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ChatRepository

class SendWebSocketMessageUseCase (
    private val repository: ChatRepository
) {
    suspend operator fun invoke(conversationId: String, content: String): Result<MessageEntity> {
        return repository.sendMessage(conversationId, content)
    }

    operator fun invoke(content: String): Boolean {
        return repository.sendWebSocketMessage(content)
    }
}