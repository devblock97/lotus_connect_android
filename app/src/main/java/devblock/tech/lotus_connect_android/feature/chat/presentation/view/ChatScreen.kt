package devblock.tech.lotus_connect_android.feature.chat.presentation.view

import android.app.Dialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import devblock.tech.lotus_connect_android.core.theme.AppColor
import devblock.tech.lotus_connect_android.core.utils.resolveMediaUrl
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity
import devblock.tech.lotus_connect_android.feature.chat.presentation.view_model.ChatViewModel
import devblock.tech.lotus_connect_android.feature.chat.presentation.widgets.BubbleMessage
import devblock.tech.lotus_connect_android.feature.chat.presentation.widgets.ChatTimestampHeader
import devblock.tech.lotus_connect_android.feature.chat.presentation.widgets.FullScreenMediaViewer
import devblock.tech.lotus_connect_android.feature.chat.presentation.widgets.MessengerTopBar
import devblock.tech.lotus_connect_android.feature.chat.presentation.widgets.TypingIndicatorBubble
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    conversation: ConversationEntity? = null,
    viewModel: ChatViewModel,
    onBackClick: () -> Unit,
    onCallClick: () -> Unit,
    onVideoCallClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    var textInput by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // Auto-scroll to the bottom when new message arrives or is sent
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    val onSendAction: () -> Unit = {
        val trimmed = textInput.trim()
        if (trimmed.isNotEmpty()) {
            viewModel.sendMessage(trimmed)
            textInput = ""
        }
    }

    var selectedMediaUrl by remember { mutableStateOf<String?>(null) }
    var fullScreenViewerData by remember { mutableStateOf<Pair<List<String>, Int>?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.MessengerBackground)
            .statusBarsPadding()
            .imePadding()
            .navigationBarsPadding()
    ) {
        // Top Header
        MessengerTopBar(
            title = conversation?.title ?: "",
            subTitle = "online",
            avatarUrl = null,
            onBackClick = onBackClick,
            onCallClick = onCallClick,
            onVideoCallClick = onVideoCallClick
        )

        // Error / Connection alert banner
        if (uiState.errorMessage != null) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { viewModel.reconnectRealtime() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Retry connection",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { viewModel.clearError() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss error",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Messages list or empty / loading states
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                uiState.isLoading && uiState.messages.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.messages.isEmpty() -> {
                    Text(
                        text = "No messages yet.\nSay hello to start the conversation!",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(
                            items = uiState.messages,
                            key = { _, msg -> msg.id}
                        ) { index, message ->
                            val currentUserId = uiState.currentUserId ?: ""
                            val prevMessage = uiState.messages.getOrNull(index - 1)
                            val nextMessage = uiState.messages.getOrNull(index + 1)
                            val mediaLists = message.mediaItems.map { it.url }

                            // Show timestamp at top or if more than 20 minutes between messages
                            val showTimestamp = prevMessage == null || shouldShowTimestamp(prevMessage.createdAt, message.createdAt)
                            if (showTimestamp) {
                                ChatTimestampHeader(timestamp = formatMessageTimestamp(message.createdAt))
                            }

                            // Incoming grouping: show avatar only on the last message of consecutive messages
                            val isLastInGroup = nextMessage == null || nextMessage.senderId != message.senderId
                            BubbleMessage(
                                message = message,
                                userId = uiState.currentUserId ?: "",
                                isLastInGroup = isLastInGroup,
                                onImageClick = { clickedIndex, allUrls ->
                                    fullScreenViewerData = Pair(mediaLists, clickedIndex)
                                }
                            )

                            if (selectedMediaUrl != null) {
                                androidx.compose.ui.window.Dialog(
                                    onDismissRequest = { selectedMediaUrl = null }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable { selectedMediaUrl = null },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = selectedMediaUrl,
                                            contentDescription = "Full preview",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Peer typing animated bubble appears at the bottom of messages
        if (uiState.isPeerTyping) {
            TypingIndicatorBubble(peerName = conversation?.title)
        }

        // Message Input Bar
        Surface(
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = {
                        textInput = it
                        viewModel.onTextInputChanged(it, conversation?.peerId)
                    },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier.weight(1f),
                    maxLines = 4,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { onSendAction() }
                    )
                )

                Spacer(modifier = Modifier.width(6.dp))

                val canSend = textInput.isNotBlank()
                IconButton(
                    onClick = onSendAction,
                    enabled = canSend,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (canSend) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (canSend) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    fullScreenViewerData?.let { (urls, index) ->
        FullScreenMediaViewer(
            mediaUrls = urls,
            initialIndex = index,
            onDismiss = { fullScreenViewerData = null }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatMessageTimestamp(createdAt: String?): String {
    if (createdAt.isNullOrBlank()) return "11:40"

    return try {
        val zoneDateTime = if (createdAt.contains("T")) {
            try {
                Instant.parse(createdAt).atZone(ZoneId.systemDefault())
            } catch (_: Exception) {
                LocalDateTime.parse(createdAt).atZone(ZoneId.systemDefault())
            }
        } else {
            LocalDate.parse(createdAt).atStartOfDay(ZoneId.systemDefault())
        }
        val now = java.time.ZonedDateTime.now(ZoneId.systemDefault())
        val timePattern = DateTimeFormatter.ofPattern("HH:mm")

        when {
            zoneDateTime.toLocalDateTime() == now.toLocalDateTime() -> {
                zoneDateTime.format(timePattern)
            }
            zoneDateTime.toLocalDateTime() == now.toLocalDateTime().minusDays(1) -> {
                "Yesterday ${zoneDateTime.format(timePattern)}"
            }
            else -> {
                zoneDateTime.format(DateTimeFormatter.ofPattern("MMM d, HH:mm"))
            }
        }
    } catch (_: Exception) {
        "11: 40"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun  shouldShowTimestamp(prevIso: String?, currentIso: String?): Boolean {
    if (prevIso.isNullOrBlank() || currentIso.isNullOrBlank()) return false

    return try {
        val prevInstant: Instant = Instant.parse(prevIso)
        val currentInstant: Instant = Instant.parse(currentIso)
        java.time.Duration.between(prevInstant, currentInstant).toMinutes() >= 20
    } catch (_: Exception) {
        false
    }
}