package devblock.tech.lotus_connect_android.feature.home.presentation.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import devblock.tech.lotus_connect_android.feature.home.domain.entities.MediaEntity
import devblock.tech.lotus_connect_android.feature.home.domain.entities.PostEntity

@Composable
fun PostCard(
    post: PostEntity,
    modifier: Modifier = Modifier
) {
    var isLiked by remember(post.id) { mutableStateOf(post.userHasLiked) }
    var likeCount by remember(post.id) { mutableIntStateOf(post.likeCount) }
    var isBookmarked by remember(post.id) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(bottom = 16.dp)
    ) {
        // Author Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Author Avatar (Photo or Initials Badge)
            if (!post.author.avatarUrl.isNullOrBlank()) {
                ImageWithLoader(
                    imageUrl = post.author.avatarUrl,
                    size = 38.dp
                )
            } else {
                val initials = post.author.username.take(2).uppercase()
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF536DFE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = post.author.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
                if (post.author.fullName.isNotBlank()) {
                    Text(
                        text = post.author.fullName,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            IconButton(
                onClick = { /* More options */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Post Media Carousel
        val mediaItems = post.mediaItems
        val pagerState = rememberPagerState(pageCount = { mediaItems.size.coerceAtLeast(1) })

        if (mediaItems.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = mediaItems[page].url,
                        contentDescription = "Post image ${page + 1}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // 1/4 page count badge in top right
                if (mediaItems.size > 1) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1}/${mediaItems.size}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Action Buttons Row: Heart, Comment, Share, Carousel Dots, Bookmark
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Heart / Like
            IconButton(
                onClick = {
                    isLiked = !isLiked
                    likeCount += if (isLiked) 1 else -1
                },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isLiked) Color(0xFFE91E63) else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Comment
            IconButton(
                onClick = { /* Open comments */ },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Share / Send
            IconButton(
                onClick = { /* Share post */ },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Carousel dots indicator
            if (mediaItems.size > 1) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(mediaItems.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 6.5.dp else 5.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Color(0xFF0095F6)
                                    else Color.White.copy(alpha = 0.35f)
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            // Bookmark
            IconButton(
                onClick = { isBookmarked = !isBookmarked },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Likes count
        Text(
            text = "$likeCount likes",
            fontWeight = FontWeight.Bold,
            fontSize = 13.5.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Caption with bold username
        if (post.content.isNotBlank()) {
            val annotatedCaption = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                    append(post.author.username)
                    append(" ")
                }
                withStyle(SpanStyle(color = Color.White)) {
                    append(post.content)
                }
            }

            Text(
                text = annotatedCaption,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun ImageWithLoader(
    imageUrl: String,
    size: Dp,
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .size(size),
        contentScale = ContentScale.Crop,
    ) {
        val state = painter.state
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }
            }
            is AsyncImagePainter.State.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.BrokenImage,
                        contentDescription = "Failed to load image",
                        tint = Color.Gray
                    )
                }
            }
            else -> SubcomposeAsyncImageContent()
        }
    }
}