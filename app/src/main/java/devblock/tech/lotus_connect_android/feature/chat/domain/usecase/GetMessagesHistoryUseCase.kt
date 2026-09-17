package devblock.tech.lotus_connect_android.feature.chat.domain.usecase

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ChatRepository

data class GetMessagesHistoryParam(
    val conversationId: String
)
class GetMessagesHistoryUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(param: GetMessagesHistoryParam): Result<List<MessageEntity>> {
        return repository.getMessagesHistory(param.conversationId)
    }
}