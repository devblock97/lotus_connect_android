package devblock.tech.lotus_connect_android.feature.chat.domain.usecase

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow

class ObserveIncomingMessageUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(conversationId: String): Flow<MessageEntity> {
        return repository.observeIncomingMessages(conversationId)
    }
}