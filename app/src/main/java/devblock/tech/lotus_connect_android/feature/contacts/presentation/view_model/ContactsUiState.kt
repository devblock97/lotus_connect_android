package devblock.tech.lotus_connect_android.feature.contacts.presentation.view_model

import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend

data class ContactsUiState(
    val isLoading: Boolean = false,
    val isPendingLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val pendingRequests: List<Friend> = emptyList(),
    val isActionLoading: Boolean = false,
    val activeActionFriendId: String? = null,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val actionSuccessMessage: String? = null,
)