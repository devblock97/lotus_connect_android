package devblock.tech.lotus_connect_android.feature.stories.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto

@Composable
fun StoryItem(
    story: StoryDto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onAddClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .width(72.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StoryAvatar(
            story = story,
            avatarSize = 68.dp,
            onAddClick = onAddClick
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = if (story.isSelf) "story" else story.user.username,
            color = Color.White,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
