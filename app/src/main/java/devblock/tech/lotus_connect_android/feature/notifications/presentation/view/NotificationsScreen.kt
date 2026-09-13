package devblock.tech.lotus_connect_android.feature.notifications.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationData
import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity
import devblock.tech.lotus_connect_android.feature.notifications.presentation.view_model.NotificationViewModel
import devblock.tech.lotus_connect_android.feature.notifications.presentation.widgets.NotificationSkeletonList

@Composable
fun LazyListState.isScrolledToBottom(): Boolean {
    val reachedBottom by remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = layoutInfo.totalItemsCount

            lastVisibleItem != null && lastVisibleItem.index == totalItems - 1
        }
    }
    return reachedBottom
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    val hasUnread = uiState.notifications.any { !it.isRead }
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItems > 0 && lastVisibleIndex >= totalItems - 1
        }.collect { isNearBottom ->
            if (isNearBottom) {
                viewModel.loadNextPage()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (hasUnread) {
                        IconButton(onClick = { viewModel.readAllNotification() }) {
                            Icon(
                                imageVector = Icons.Filled.DoneAll,
                                contentDescription = "Mark all as read",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.getNotifications() },
            modifier = modifier.fillMaxSize().padding(innerPadding),
        ) {
            if (uiState.notifications.isEmpty() && !uiState.isLoading && !uiState.isRefreshing) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notifications yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (uiState.isLoading && uiState.notifications.isEmpty()) {
                NotificationSkeletonList()
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    state = listState
                ) {
                    items(uiState.notifications, key = { it.id }) { notification ->
                        SwipeableRow(
                            onDelete = { viewModel.deleteNotification(notification.id) },
                        ) {
                            NotificationRow(
                                notification = notification,
                                onClick = {
                                    if (!notification.isRead) {
                                        viewModel.readNotification(notification.id)
                                    }
                                }
                            )
                        }
                    }

                    if (uiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableRow(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> { onDelete(); true }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val isDismissing = dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart
            val color = if (isDismissing) MaterialTheme.colorScheme.errorContainer else Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (isDismissing) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Notification",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            content()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun NotificationRow(
    notification: NotificationEntity,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (!notification.isRead)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f)
                else
                    MaterialTheme.colorScheme.surface
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(iconBackgroundColor(notification.data)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconForType(notification.data),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Normal,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = notification.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (!notification.isRead) {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

    }

    Spacer(modifier = Modifier.height(4.dp))

}

private fun iconForType(type: NotificationData): ImageVector {
    when (type.type) {
        "chat" -> {
            return Icons.AutoMirrored.Filled.Chat
        }
        "missed_call" -> {
            return Icons.AutoMirrored.Filled.CallMissed
        }
        "friend_accept" -> {
            return Icons.Filled.Person2
        }
        "friend_request" -> {
            return Icons.Filled.PersonAdd
        }
        else -> {
            return Icons.Filled.VideoCall
        }
    }
}


private fun iconBackgroundColor(type: NotificationData): Color {
    when (type.type) {
        "chat" -> {
            return Color(0xFF4285F4)
        }
        "missed_call" -> {
            return Color(0xFFEA4335)
        }
        "friend_accept" -> {
            return Color(0xFF34A853)
        }
        "friend_request" -> {
            return Color(0xFF9AA0A6)
        }
        else -> {
            return Color(0xFFEA4335)
        }
    }
}
