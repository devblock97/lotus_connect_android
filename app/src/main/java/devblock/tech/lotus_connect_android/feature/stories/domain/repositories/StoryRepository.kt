package devblock.tech.lotus_connect_android.feature.stories.domain.repositories

import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto
import devblock.tech.lotus_connect_android.feature.stories.domain.entities.StoryEntity

interface StoryRepository {
    suspend fun getStories(): Result<List<StoryDto>>
}