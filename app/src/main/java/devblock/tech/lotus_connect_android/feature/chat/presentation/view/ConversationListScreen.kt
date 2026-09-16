package devblock.tech.lotus_connect_android.feature.chat.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devblock.tech.lotus_connect_android.core.utils.gradientFor
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity
import devblock.tech.lotus_connect_android.feature.chat.presentation.view_model.ConversationListViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListScreen(
    viewModel: ConversationListViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Conversations",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isSuccess && uiState.conversations.isNotEmpty() && !uiState.isLoading) {
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(uiState.conversations, key = { it.id }) { conversation ->
                    ConversationCard(conversation = conversation)
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
private fun toDisplayDate(input: String?): String {
    if (input.isNullOrBlank()) return ""
    return try {
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        if (input.contains("T")) {
            // ISO-8601 instant/offset format: e.g. "2026-08-24T09:49:24.194704Z"
            try {
                Instant.parse(input).atZone(ZoneId.systemDefault()).format(outputFormatter)
            } catch (_: Exception) {
                LocalDateTime.parse(input).format(outputFormatter)
            }
        } else {
            // Date-only input: "2024-06-15"
            val date = LocalDate.parse(input)
            date.format(outputFormatter)
        }
    } catch (e: Exception) {
        // Fallback: safely extract YYYY-MM-DD or return raw input if parsing fails
        if (input.length >= 10 && input[4] == '-' && input[7] == '-') {
            val parts = input.take(10).split("-")
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } else {
            input
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ConversationCard(
    conversation: ConversationEntity
) {

    val gradient = remember(conversation.id) { gradientFor(conversation.id) }
    val date = remember(conversation.createdAt) {
        toDisplayDate(conversation.createdAt)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = gradient,
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = conversation.title.take(1).uppercase(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = conversation.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Hello friend :)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = date,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}