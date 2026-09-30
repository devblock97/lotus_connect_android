package devblock.tech.lotus_connect_android.feature.stories.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto

private val StoryGradientColors = listOf(
    Color(0xFFDE0046),
    Color(0xFFF7A34B),
    Color(0xFFFF3D00),
    Color(0xFFD81B60),
    Color(0xFF8E24AA),
    Color(0xFFDE0046)
)

private val CloseFriendGradientColors = listOf(
    Color(0xFF10B981),
    Color(0xFF059669)
)

@Composable
fun StoryAvatar(
    story: StoryDto,
    modifier: Modifier = Modifier,
    avatarSize: Dp = 68.dp,
    onAddClick: (() -> Unit)? = null
) {
    val hasRing = story.hasUnseen || (!story.isSelf && story.stories.isNotEmpty())
    val isUnseen = story.hasUnseen

    val ringBrush = when {
        story.hasCloseFriendStory -> Brush.linearGradient(CloseFriendGradientColors)
        isUnseen -> Brush.sweepGradient(StoryGradientColors)
        else -> Brush.linearGradient(listOf(Color(0xFF424242), Color(0xFF424242)))
    }

    Box(
        modifier = modifier.size(avatarSize),
        contentAlignment = Alignment.Center
    ) {
        // Outer ring (if has stories or unseen)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (hasRing) {
                        Modifier
                            .border(
                                width = if (isUnseen) 2.5.dp else 1.5.dp,
                                brush = ringBrush,
                                shape = CircleShape
                            )
                            .padding(if (isUnseen) 3.5.dp else 2.5.dp)
                    } else {
                        Modifier
                    }
                )
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            // Avatar content
            StoryAvatarContent(
                avatarUrl = story.user.avatarUrl,
                username = story.user.username
            )
        }

        // Plus badge on self story
        if (story.isSelf) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 1.dp, y = 1.dp)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0095F6))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onAddClick?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Story",
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
private fun StoryAvatarContent(
    avatarUrl: String?,
    username: String
) {
    if (!avatarUrl.isNullOrBlank()) {
        SubcomposeAsyncImage(
            model = avatarUrl,
            contentDescription = username,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF262626)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                }
                is AsyncImagePainter.State.Error -> {
                    DefaultAvatarPlaceholder(username = username)
                }
                else -> SubcomposeAsyncImageContent()
            }
        }
    } else {
        DefaultAvatarPlaceholder(username = username)
    }
}

@Composable
private fun DefaultAvatarPlaceholder(username: String) {
    if (username.equals("marvel", ignoreCase = true)) {
        // Red Marvel branded circular icon
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Color(0xFFE23636)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MARVEL",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
        }
    } else {
        // Subtle dark circle with silhouette person icon
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Color(0xFF2A2A2A)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = username,
                tint = Color(0xFFDDDDDD),
                modifier = Modifier.size(34.dp)
            )
        }
    }
}
