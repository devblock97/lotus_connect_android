package devblock.tech.lotus_connect_android.feature.contacts.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.core.view_model.BaseViewModel
import devblock.tech.lotus_connect_android.feature.contacts.data.datasources.ContactsRemoteDataSourceImpl
import devblock.tech.lotus_connect_android.feature.contacts.data.repositories.ContactsRepositoryImpl
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.AcceptFriendParam
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.AcceptFriendUseCase
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.DeleteFriendParam
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.DeleteFriendUseCase
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.GetFriendUseCase
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.GetRequestersListUseCase
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.RejectFriendParam
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.RejectFriendUseCase
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.SendFriendRequestParam
import devblock.tech.lotus_connect_android.feature.contacts.domain.usecase.SendFriendRequestUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val getFriendUseCase: GetFriendUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val acceptFriendUseCase: AcceptFriendUseCase,
    private val rejectFriendUseCase: RejectFriendUseCase,
    private val deleteFriendUseCase: DeleteFriendUseCase,
    private val getRequestersListUseCase: GetRequestersListUseCase
): BaseViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        getFriendsList()
        getRequestersList()
    }

    fun getFriendsList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = getFriendUseCase()

            result.fold(
                onSuccess = { friends ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            friends = friends
                        )
                    }
                },
                onFailure = { error ->
                    handleException(error, defaultMessage = "Failed to load friends list") { message, _ ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                errorMessage = message
                            )
                        }
                    }
                }
            )
        }
    }

    fun getRequestersList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isPendingLoading = true, errorMessage = null) }

            val result = getRequestersListUseCase()

            result.fold(
                onSuccess = { requesters ->
                    _uiState.update { state ->
                        state.copy(
                            isPendingLoading = false,
                            pendingRequests = requesters
                        )
                    }
                },
                onFailure = { error ->
                    handleException(error, defaultMessage = "Failed to get requesters list") { message, _ ->
                        _uiState.update { state ->
                            state.copy(
                                isPendingLoading = false,
                                errorMessage = message
                            )
                        }
                    }
                }
            )
        }
    }

    fun acceptFriend(friendId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isActionLoading = true, activeActionFriendId = friendId, errorMessage = null) }

            val result = acceptFriendUseCase(
                param = AcceptFriendParam(friendId = friendId)
            )

            result.fold(
                onSuccess = { data ->
                    _uiState.update { state ->
                        state.copy(
                            isActionLoading = false,
                            activeActionFriendId = null,
                            pendingRequests = state.pendingRequests.filterNot { it.id == friendId },
                            actionSuccessMessage = data.message.ifBlank { "Friend request accepted" }
                        )
                    }
                    getFriendsList()
                    getRequestersList()
                },
                onFailure = { error ->
                    handleException(error, defaultMessage = "Failed to accept friend") { message, _ ->
                        _uiState.update { state ->
                            state.copy(
                                isActionLoading = false,
                                activeActionFriendId = null,
                                errorMessage = message
                            )
                        }
                    }
                }
            )
        }
    }

    fun rejectFriend(friendId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isActionLoading = true, activeActionFriendId = friendId, errorMessage = null) }

            val result = rejectFriendUseCase(
                param = RejectFriendParam(friendId = friendId)
            )

            result.fold(
                onSuccess = { data ->
                    _uiState.update { state ->
                        state.copy(
                            isActionLoading = false,
                            activeActionFriendId = null,
                            pendingRequests = state.pendingRequests.filterNot { it.id == friendId },
                            actionSuccessMessage = data.message.ifBlank { "Friend request declined" }
                        )
                    }
                    getRequestersList()
                },
                onFailure = { error ->
                    handleException(error, defaultMessage = "Failed to reject friend") { message, _ ->
                        _uiState.update { state ->
                            state.copy(
                                isActionLoading = false,
                                activeActionFriendId = null,
                                errorMessage = message
                            )
                        }
                    }
                }
            )
        }
    }

    fun sendFriendRequest(username: String, onComplete: (Boolean) -> Unit = {}) {
        val trimmed = username.trim()
        if (trimmed.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Username cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isActionLoading = true, errorMessage = null) }

            val result = sendFriendRequestUseCase(
                param = SendFriendRequestParam(username = trimmed)
            )

            result.fold(
                onSuccess = { data ->
                    _uiState.update { state ->
                        state.copy(
                            isActionLoading = false,
                            actionSuccessMessage = data.message.ifBlank { "Friend request sent to $trimmed" }
                        )
                    }
                    getRequestersList()
                    onComplete(true)
                },
                onFailure = { error ->
                    handleException(error, defaultMessage = "Failed to send friend request") { message, _ ->
                        _uiState.update { state ->
                            state.copy(
                                isActionLoading = false,
                                errorMessage = message
                            )
                        }
                    }
                    onComplete(false)
                }
            )
        }
    }

    fun deleteFriend(friendId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isActionLoading = true, activeActionFriendId = friendId, errorMessage = null) }

            val result = deleteFriendUseCase(
                param = DeleteFriendParam(friendId = friendId)
            )

            result.fold(
                onSuccess = { data ->
                    _uiState.update { state ->
                        state.copy(
                            isActionLoading = false,
                            activeActionFriendId = null,
                            friends = state.friends.filterNot { it.id == friendId },
                            actionSuccessMessage = data.message.ifBlank { "Friend removed" }
                        )
                    }
                    getFriendsList()
                },
                onFailure = { error ->
                    handleException(error, defaultMessage = "Failed to delete friend") { message, _ ->
                        _uiState.update { state ->
                            state.copy(
                                isActionLoading = false,
                                activeActionFriendId = null,
                                errorMessage = message
                            )
                        }
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearActionSuccessMessage() {
        _uiState.update { it.copy(actionSuccessMessage = null) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val apiService = RetrofitClient.contactsApiService
                val remoteDataSource = ContactsRemoteDataSourceImpl(apiService)
                val repository = ContactsRepositoryImpl(
                    remoteDataSource = remoteDataSource
                )
                val getFriendUseCase = GetFriendUseCase(repository)
                val sendFriendRequestUseCase = SendFriendRequestUseCase(repository)
                val acceptFriendUseCase = AcceptFriendUseCase(repository)
                val rejectFriendUseCase = RejectFriendUseCase(repository)
                val deleteFriendUseCase = DeleteFriendUseCase(repository)
                val getRequestersListUseCase = GetRequestersListUseCase(repository)

                return ContactsViewModel(
                    getFriendUseCase = getFriendUseCase,
                    sendFriendRequestUseCase = sendFriendRequestUseCase,
                    acceptFriendUseCase = acceptFriendUseCase,
                    rejectFriendUseCase = rejectFriendUseCase,
                    deleteFriendUseCase = deleteFriendUseCase,
                    getRequestersListUseCase = getRequestersListUseCase
                ) as T
            }
        }
    }
}