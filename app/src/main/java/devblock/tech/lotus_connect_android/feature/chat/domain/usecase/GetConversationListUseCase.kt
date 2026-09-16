package devblock.tech.lotus_connect_android.feature.chat.domain.usecase

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ConversationRepository

class GetConversationListUseCase(
    private val repository: ConversationRepository
) {
    suspend operator fun invoke(): Result<List<ConversationEntity>> {
        return repository.getConversationList()
    }
}