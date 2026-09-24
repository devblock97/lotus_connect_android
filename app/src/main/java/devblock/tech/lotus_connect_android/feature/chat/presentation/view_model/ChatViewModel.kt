package devblock.tech.lotus_connect_android.feature.chat.presentation.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.auth.data.datasources.AuthLocalDataSource
import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ChatRemoteDataSourceImpl
import devblock.tech.lotus_connect_android.feature.chat.data.repositories.ChatRepositoryImpl
import devblock.tech.lotus_connect_android.feature.chat.domain.usecase.GetMessagesHistoryParam
import devblock.tech.lotus_connect_android.feature.chat.domain.usecase.GetMessagesHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class ChatViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMessagesHistoryUseCase: GetMessagesHistoryUseCase,
    currentUserId: String?
): ViewModel() {

    private val conversationId: String = checkNotNull(savedStateHandle["conversationId"])
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(currentUserId = currentUserId) }
    }

    init {
        getMessages(conversationId)
    }

    private fun getMessages(conversationId: String) {
        viewModelScope.launch {
            try {
                val result = getMessagesHistoryUseCase(
                    GetMessagesHistoryParam(conversationId)
                )

                result.fold(
                    onSuccess = { messages ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                messages = messages
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Something went wrong. Please try again"
                            )
                        }
                    }
                )

            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.localizedMessage ?: "An unknown error occurred") }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val savedStateHandle = extras.createSavedStateHandle()
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val localDataSource = AuthLocalDataSource(application.applicationContext)
                val currentUserId = localDataSource.getCachedUser()?.id
                val chatService = RetrofitClient.chatApiService
                val remoteDataSource = ChatRemoteDataSourceImpl(chatService = chatService)
                val repository = ChatRepositoryImpl(
                    remoteDataSource = remoteDataSource
                )
                val getMessagesHistoryUseCase = GetMessagesHistoryUseCase(
                    repository = repository
                )
                return ChatViewModel(
                    savedStateHandle = savedStateHandle,
                    getMessagesHistoryUseCase = getMessagesHistoryUseCase,
                    currentUserId = currentUserId
                ) as T
            }
        }
    }
}