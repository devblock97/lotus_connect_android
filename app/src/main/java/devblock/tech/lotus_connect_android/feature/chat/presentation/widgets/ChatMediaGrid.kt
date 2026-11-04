package devblock.tech.lotus_connect_android.feature.chat.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import devblock.tech.lotus_connect_android.core.utils.resolveMediaUrl

@Composable
fun ChatMediaGrid(
    mediaUrl: List<String>,
    modifier: Modifier = Modifier,
    onImageClick: ((Int) -> Unit)? = null
) {
    if (mediaUrl.isEmpty()) return

    println("check media grid: ${mediaUrl.size}")

    if (mediaUrl.size == 1) {
        val resolvedUrl = remember(mediaUrl.first()) {
            resolveMediaUrl(mediaUrl.first()) ?: mediaUrl.first()
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 180.dp, max = 300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.05f))
                .clickable(enabled = onImageClick != null) { onImageClick?.invoke(0)},
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = resolvedUrl,
                contentDescription = "Media image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is AsyncImagePainter.State.Error -> {
                        Icon(
                            imageVector = Icons.Default.BrokenImage,
                            contentDescription = "Failed to load",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    else -> SubcomposeAsyncImageContent()
                }
            }
        }
    } else {
        val columns = when (mediaUrl.size) {
            2 -> 2
            3 -> 3
            else -> 4
        }

        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            mediaUrl.chunked(columns).forEachIndexed { rowIndex, rowUrls ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    rowUrls.forEachIndexed { colIndex, url ->
                        val absoluteIndex = (rowIndex * columns) + colIndex
                        val resolvedUrl = remember(url) { resolveMediaUrl(url) }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Black.copy(alpha = 0.05f))
                                .clickable(enabled = onImageClick != null) {
                                    onImageClick?.invoke(absoluteIndex)
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            SubcomposeAsyncImage(
                                model = resolvedUrl,
                                contentDescription = "Media thumbnail",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                when (painter.state) {
                                    is AsyncImagePainter.State.Loading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(8.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    is AsyncImagePainter.State.Error -> {
                                        Icon(
                                            imageVector = Icons.Default.BrokenImage,
                                            contentDescription = "Failed to load",
                                            tint = MaterialTheme.colorScheme.outline,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    else -> SubcomposeAsyncImageContent()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}