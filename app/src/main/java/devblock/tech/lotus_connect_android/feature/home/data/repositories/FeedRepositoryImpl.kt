package devblock.tech.lotus_connect_android.feature.home.data.repositories

import devblock.tech.lotus_connect_android.feature.home.data.datasources.FeedRemoteDataSource
import devblock.tech.lotus_connect_android.feature.home.domain.entities.PostEntity
import devblock.tech.lotus_connect_android.feature.home.domain.repositories.FeedRepository

class FeedRepositoryImpl(
    val remoteDataSource: FeedRemoteDataSource
) : FeedRepository {

    override suspend fun getFeed(): Result<List<PostEntity>> = runCatching {
        val response = remoteDataSource.getFeed()
        response
    }
}