package devblock.tech.lotus_connect_android.feature.chat.domain.usecase

import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow

class ObserveTypingStatusUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(conversationId: String): Flow<Boolean> {
        return repository.observeTypingStatus(conversationId)
    }
}