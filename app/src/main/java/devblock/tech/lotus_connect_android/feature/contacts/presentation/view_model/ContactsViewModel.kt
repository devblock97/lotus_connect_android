package devblock.tech.lotus_connect_android.feature.contacts.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.contacts.data.datasources.ContactsRemoteDataSource
import devblock.tech.lotus_connect_android.feature.contacts.data.repositories.ContactsRepositoryImpl
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.GetFriendUseCase
import devblock.tech.lotus_connect_android.feature.notifications.data.datasources.NotificationRemoteDataSource
import devblock.tech.lotus_connect_android.feature.notifications.data.repositories.NotificationRepositoryImpl
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.DeleteAllNotificationsUseCase
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.DeleteNotificationUseCase
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.GetNotificationsUseCase
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.ReadAllNotificationsUseCase
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.ReadNotificationUseCase
import devblock.tech.lotus_connect_android.feature.notifications.presentation.view_model.NotificationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val getFriendUseCase: GetFriendUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    init {
        getFriendsList()
    }

    fun getFriendsList() {
        viewModelScope.launch {
            val result = getFriendUseCase()

            result.fold(
                onFailure = { error ->
                    println("get friend list failure: ${error.message}")
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load friend"
                        )
                    }
                },
                onSuccess = { friends ->
                    println("get friend list success")
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            friends = friends
                        )
                    }
                }
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val apiService = RetrofitClient.contactsApiService
                val remoteDataSource = ContactsRemoteDataSource(apiService)
                val repository = ContactsRepositoryImpl(
                    remoteDataSource = remoteDataSource
                )
                val getFriendUseCase = GetFriendUseCase(repository)


                return ContactsViewModel(
                    getFriendUseCase = getFriendUseCase,
                ) as T
            }
        }
    }
}