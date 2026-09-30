package devblock.tech.lotus_connect_android.feature.stories.data.repositories

import devblock.tech.lotus_connect_android.feature.stories.data.datasources.StoryRemoteDataSource
import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto
import devblock.tech.lotus_connect_android.feature.stories.domain.entities.StoryEntity
import devblock.tech.lotus_connect_android.feature.stories.domain.repositories.StoryRepository

class StoryRepositoryImpl(
    private val remoteDataSource: StoryRemoteDataSource
) : StoryRepository {

    override suspend fun getStories(): Result<List<StoryDto>> = runCatching {
        val response = remoteDataSource.getStories()
        response
    }
}