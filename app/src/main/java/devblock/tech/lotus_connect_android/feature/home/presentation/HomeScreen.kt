package devblock.tech.lotus_connect_android.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import devblock.tech.lotus_connect_android.feature.home.domain.entities.MediaEntity
import devblock.tech.lotus_connect_android.feature.home.domain.entities.PostEntity
import devblock.tech.lotus_connect_android.feature.home.presentation.view_model.FeedViewModel
import devblock.tech.lotus_connect_android.feature.home.presentation.widget.PostCard

@Composable fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel
) {

    val uiState by viewModel.uiState.collectAsState()


    if (uiState.feeds.isNotEmpty() && !uiState.isLoading) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(uiState.feeds) { post ->
                PostCard(post = post)
            }
        }
    }

    if (uiState.isLoading) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
