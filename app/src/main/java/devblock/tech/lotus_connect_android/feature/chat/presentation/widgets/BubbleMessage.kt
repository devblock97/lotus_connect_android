package devblock.tech.lotus_connect_android.feature.chat.presentation.widgets

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devblock.tech.lotus_connect_android.core.utils.toDisplayDate
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BubbleMessage(
    message: MessageEntity,
    userId: String,
    isLastInGroup: Boolean,
    modifier: Modifier = Modifier,
    onImageClick: ((Int, List<String>) -> Unit)? = null
) {
    val date = remember(message.createdAt) {
        toDisplayDate(message.createdAt)
    }
    val isMe = message.senderId == userId
    val isPending = message.id.startsWith("temp_")

    // Extract text & any image URLs if present
    val (cleanText, imageUrls) = remember (message.content) {
        parseMessageContent(message.content)
    }

    val backgroundColor = if (isMe) {
        if (isPending) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        else MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (isMe) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val alignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart

    val bubbleShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isMe) 16.dp else 4.dp,
        bottomEnd = if (isMe) 4.dp else 16.dp
    )

    val allMediaUrls = remember(message) {
        when {
            !message.mediaItems.isNotEmpty() -> message.mediaItems.map { it.url }
            !message.mediaUrl.isNullOrBlank() -> listOf(message.mediaUrl)
            else -> imageUrls
        }
    }

    var selectedMediaUrl by remember { mutableStateOf<String?>(null) }

    Log.d("check media list", "all media url: ${allMediaUrls.size}")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {

            Column(
                modifier = Modifier
                    .clip(bubbleShape)
                    .background(backgroundColor)
            ) {
                // Media Grid (1 large photo or multi-photo grid)
                if (message.mediaItems.isNotEmpty()) {
                    ChatMediaGrid(
                        mediaUrl = message.mediaItems.map { it.url },
                        onImageClick = { index -> onImageClick?.invoke(index, allMediaUrls)}
                    )
                }

                // Display Text Caption (or raw message if no data)
                if (cleanText.isNotBlank()) {
                    Text(
                        text = cleanText,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Timestamp and pending status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 10.sp
                )

                if (isPending) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Sending...",
                        modifier = Modifier.size(10.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }

}

private fun parseMessageContent(content: String): Pair<String, List<String>> {
    println("check parse message content: $content")
    val trimmed = content.trim()
    val urlRegex = Regex("""https?://\S+""", RegexOption.IGNORE_CASE)
    val matches = urlRegex.findAll(trimmed).map { it.value }.toList()
    val imageUrls = matches.filter { url ->
        url.contains(".jpg", ignoreCase = true) ||
                url.contains(".jpeg", ignoreCase = true) ||
                url.contains(".png", ignoreCase = true) ||
                url.contains(".webp", ignoreCase = true) ||
                url.contains("/images/", ignoreCase = true) ||
                url.contains("images.unsplash.com", ignoreCase = true)
    }
    var cleanText = trimmed
    imageUrls.forEach { url ->
        cleanText = cleanText.replace(url, "").trim()
    }

    return Pair(cleanText, imageUrls)
}