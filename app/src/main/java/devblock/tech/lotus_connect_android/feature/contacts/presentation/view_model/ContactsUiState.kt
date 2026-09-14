package devblock.tech.lotus_connect_android.feature.contacts.presentation.view_model

import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend

data class ContactsUiState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = arrayListOf(),
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)