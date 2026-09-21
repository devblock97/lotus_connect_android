package devblock.tech.lotus_connect_android.feature.home.domain.usecases

import devblock.tech.lotus_connect_android.feature.home.domain.entities.PostEntity
import devblock.tech.lotus_connect_android.feature.home.domain.repositories.FeedRepository

class GetFeedUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(): Result<List<PostEntity>> {
        return repository.getFeed()
    }
}