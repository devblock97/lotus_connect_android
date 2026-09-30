package devblock.tech.lotus_connect_android.feature.stories.data.service

import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto
import retrofit2.Response
import retrofit2.http.GET

interface StoryService {
    @GET("stories/tray")
    suspend fun getStories() : Response<List<StoryDto>>
}