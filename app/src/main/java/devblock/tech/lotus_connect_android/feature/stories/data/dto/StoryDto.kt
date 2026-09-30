package devblock.tech.lotus_connect_android.feature.stories.data.dto

import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User
import devblock.tech.lotus_connect_android.feature.stories.domain.entities.StoryEntity

data class StoryDto(
    val user: User,
    val stories: List<StoryEntity>,
    val hasUnseen: Boolean,
    val totalStories: Int,
    val latestStoryCreatedAt: String,
    val hasCloseFriendStory: Boolean,
    val isSelf: Boolean
)