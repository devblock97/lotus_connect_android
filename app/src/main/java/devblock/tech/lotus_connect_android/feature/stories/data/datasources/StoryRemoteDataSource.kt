package devblock.tech.lotus_connect_android.feature.stories.data.datasources

import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto
import devblock.tech.lotus_connect_android.feature.stories.data.service.StoryService
import devblock.tech.lotus_connect_android.feature.stories.domain.entities.StoryEntity

interface StoryRemoteDataSource {
    suspend fun getStories(): List<StoryDto>
}

class StoryRemoteDataSourceImpl(
    private val storyService: StoryService
) : StoryRemoteDataSource {

    override suspend fun getStories(): List<StoryDto> {
        val response = storyService.getStories()
        if (response.isSuccessful && response.body() != null) {
            return  response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Failed to load stories")
        }
    }
}