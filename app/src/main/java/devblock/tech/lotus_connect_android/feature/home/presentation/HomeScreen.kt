package devblock.tech.lotus_connect_android.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import devblock.tech.lotus_connect_android.feature.home.domain.entities.Author
import devblock.tech.lotus_connect_android.feature.home.domain.entities.MediaEntity
import devblock.tech.lotus_connect_android.feature.home.domain.entities.PostEntity
import devblock.tech.lotus_connect_android.feature.home.presentation.view_model.FeedViewModel
import devblock.tech.lotus_connect_android.feature.home.presentation.widget.PostCard
import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto
import devblock.tech.lotus_connect_android.feature.stories.presentation.view_model.StoryViewModel
import devblock.tech.lotus_connect_android.feature.stories.presentation.widgets.StoryTray
import devblock.tech.lotus_connect_android.feature.stories.presentation.widgets.StoryViewerModal

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel,
    storyViewModel: StoryViewModel = viewModel(factory = StoryViewModel.Factory),
    onNavigateToChat: (() -> Unit)? = null,
    onNavigateToNotifications: (() -> Unit)? = null
) {
    val feedUiState by viewModel.uiState.collectAsState()
    val storyUiState by storyViewModel.uiState.collectAsState()

    var selectedStoryUser by remember { mutableStateOf<StoryDto?>(null) }
    var showAddStoryDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeTopBar(
                onAlertsClick = { onNavigateToNotifications?.invoke() },
                onMessagesClick = { onNavigateToChat?.invoke() }
            )

            // Feed Content with Story Tray at top
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Stories Row
                item(key = "story_tray_header") {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        StoryTray(
                            stories = storyUiState.stories,
                            onStoryClick = { story ->
                                selectedStoryUser = story
                            },
                            onAddStoryClick = {
                                showAddStoryDialog = true
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 4.dp),
                            color = Color(0xFF1C1C1E),
                            thickness = 0.5.dp
                        )
                    }
                }

                // Posts List
                items(
                    items = feedUiState.feeds,
                    key = { it.id }
                ) { post ->
                    PostCard(post = post)
                }

                // Extra bottom spacing for navigation bar
                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }

        // Loading overlay if initial loading
        if (feedUiState.isLoading && feedUiState.feeds.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        // Fullscreen Story Viewer Modal
        selectedStoryUser?.let { story ->
            StoryViewerModal(
                story = story,
                onDismiss = { selectedStoryUser = null }
            )
        }

        // Add Story Dialog
        if (showAddStoryDialog) {
            AddStoryDialog(
                onDismiss = { showAddStoryDialog = false }
            )
        }
    }
}

@Composable
private fun HomeTopBar(
    onAlertsClick: () -> Unit,
    onMessagesClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App title in Serif
        Text(
            text = "Lotus Connect",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))

        // Heart / Notifications Icon
        IconButton(
            onClick = onAlertsClick,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // DM / Send Icon
        IconButton(
            onClick = onMessagesClick,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = "Messages",
                tint = Color.White,
                modifier = Modifier.size(23.dp)
            )
        }
    }
}

@Composable
private fun AddStoryDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E1E1E),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add to your story",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color(0xFF2E2E2E), RoundedCornerShape(28.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Camera",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Camera", color = Color.White, fontSize = 12.sp)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color(0xFF2E2E2E), RoundedCornerShape(28.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = "Gallery",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Gallery", color = Color.White, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = Color(0xFF0095F6))
                }
            }
        }
    }
}
