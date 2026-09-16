package devblock.tech.lotus_connect_android.feature.chat.presentation.view_model

import devblock.tech.lotus_connect_android.feature.chat.domain.entities.ConversationEntity

data class ConversationListUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val conversations: List<ConversationEntity> = emptyList()
)