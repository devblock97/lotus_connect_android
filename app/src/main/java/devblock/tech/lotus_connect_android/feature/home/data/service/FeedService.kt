package devblock.tech.lotus_connect_android.feature.home.data.service

import devblock.tech.lotus_connect_android.feature.home.domain.entities.PostEntity
import retrofit2.Response
import retrofit2.http.GET

interface FeedService {
    @GET("feed")
    suspend fun getFeed(): Response<List<PostEntity>>
}