package devblock.tech.lotus_connect_android.feature.stories.presentation.widgets
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User
import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto

@Composable
fun StoryTray(
    stories: List<StoryDto>,
    modifier: Modifier = Modifier,
    onStoryClick: (StoryDto) -> Unit,
    onAddStoryClick: () -> Unit
) {
    if (stories.none { it.isSelf }) {
        val selfStory = StoryDto(
            user = User(
                id = "self",
                username = "story",
                email = "",
                fullName = "Your Story",
                avatarUrl = null
            ),
            stories = emptyList(),
            hasUnseen = false,
            totalStories = 0,
            latestStoryCreatedAt = "",
            hasCloseFriendStory = false,
            isSelf = true
        )
        listOf(selfStory) + stories
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            items = stories,
            key = { it.user.id.ifEmpty { it.user.username } }
        ) { story ->
            StoryItem(
                story = story,
                onClick = {
                    if (story.isSelf && story.stories.isEmpty()) {
                        onAddStoryClick()
                    } else {
                        onStoryClick(story)
                    }
                },
                onAddClick = onAddStoryClick
            )
        }
    }
}
