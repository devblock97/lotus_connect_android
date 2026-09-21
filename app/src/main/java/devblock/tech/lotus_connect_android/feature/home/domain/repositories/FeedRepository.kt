package devblock.tech.lotus_connect_android.feature.home.domain.repositories

import devblock.tech.lotus_connect_android.feature.home.domain.entities.PostEntity


interface FeedRepository {
    suspend fun getFeed(): Result<List<PostEntity>>
}
