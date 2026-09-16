package devblock.tech.lotus_connect_android.feature.chat.data.repositories

import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ConversationListRemoteDataSource
import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ConversationListRemoteDataSourceImpl
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ConversationRepository

class ConversationListRepositoryImpl(
    private val remoteDataSource: ConversationListRemoteDataSource
) : ConversationRepository {

    override suspend fun getConversationList(): Result<List<ConversationEntity>> = runCatching {
        val response = remoteDataSource.getConversationsList()
        response
    }
}