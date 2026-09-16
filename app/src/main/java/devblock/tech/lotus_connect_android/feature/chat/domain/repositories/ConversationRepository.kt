package devblock.tech.lotus_connect_android.feature.chat.domain.repositories

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity

interface ConversationRepository {
    suspend fun getConversationList(): Result<List<ConversationEntity>>
}