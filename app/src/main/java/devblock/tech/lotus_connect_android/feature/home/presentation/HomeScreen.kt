package devblock.tech.lotus_connect_android.feature.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
