package devblock.tech.lotus_connect_android.feature.chat.presentation.view_model

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<MessageEntity> = emptyList(),
    val errorMessage: String? = null,
    val currentUserId: String? = null,
)
