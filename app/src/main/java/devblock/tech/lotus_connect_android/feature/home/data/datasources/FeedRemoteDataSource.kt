package devblock.tech.lotus_connect_android.feature.home.data.datasources

import devblock.tech.lotus_connect_android.feature.home.data.service.FeedService
import devblock.tech.lotus_connect_android.feature.home.domain.entities.PostEntity

interface FeedRemoteDataSource {
    suspend fun getFeed(): List<PostEntity>
}

class FeedRemoteDataSourceImpl(
    private val feedService: FeedService
) : FeedRemoteDataSource {

    override suspend fun getFeed(): List<PostEntity> {
        println("triggered feed")
        val response = feedService.getFeed()

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to get feed with error: ${response.errorBody()?.string()}")
        }
    }
}