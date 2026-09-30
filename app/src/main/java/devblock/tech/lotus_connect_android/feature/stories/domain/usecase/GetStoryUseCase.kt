package devblock.tech.lotus_connect_android.feature.stories.domain.usecase
import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto
import devblock.tech.lotus_connect_android.feature.stories.domain.repositories.StoryRepository

class GetStoryUseCase(private val storyRepository: StoryRepository) {
    suspend operator fun invoke(): Result<List<StoryDto>> {
        return storyRepository.getStories()
    }
}