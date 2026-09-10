package devblock.tech.lotus_connect_android.feature.notifications.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationData
import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity
import devblock.tech.lotus_connect_android.feature.notifications.presentation.view_model.NotificationViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.notifications.isEmpty()) {
        Text("Empty notifications")
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(uiState.notifications, key = { it.id }) { notification ->
            NotificationRow(notification)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun NotificationRow(
    notification: NotificationEntity
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (!notification.isRead)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                else
                    Color.Transparent
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

        Column() {
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
    }

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
