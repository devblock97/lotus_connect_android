package devblock.tech.lotus_connect_android.feature.chat.domain.usecase

import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ChatRepository

class SendTypingStatusUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(recipientId: String, conversationId: String, isTyping: Boolean): Boolean {
        return repository.sendTypingStatus(recipientId, conversationId, isTyping)
    }
}