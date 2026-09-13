package devblock.tech.lotus_connect_android.feature.notifications.presentation.view_model

import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationEntity> = arrayListOf(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean= false,
    val nextCursor: String? = null,
    val hasMore: Boolean = false,
    val isLoadingMore: Boolean = false,
)
