package devblock.tech.lotus_connect_android.feature.chat.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ConversationListRemoteDataSource
import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ConversationListRemoteDataSourceImpl
import devblock.tech.lotus_connect_android.feature.chat.data.repositories.ConversationListRepositoryImpl
import devblock.tech.lotus_connect_android.feature.chat.domain.repositories.ConversationRepository
import devblock.tech.lotus_connect_android.feature.chat.domain.usecase.GetConversationListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import devblock.tech.lotus_connect_android.core.view_model.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConversationListViewModel(
    private val getConversationListUseCase: GetConversationListUseCase
): BaseViewModel() {

    private val _uiState = MutableStateFlow(ConversationListUiState())
    val uiState: StateFlow<ConversationListUiState> = _uiState.asStateFlow()

    init {
        getConversationList()
    }

    fun getConversationList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = getConversationListUseCase()

            result.fold(
                onSuccess = { conversations ->
                    println("Display conversation list success: ${conversations.size}")
                    _uiState.update {
                        it.copy(
                            conversations = conversations,
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                },
                onFailure = { error ->
                    println("Display conversation list error: ${error.message}")
                    handleException(error, defaultMessage = "Failed to load conversation list") { message, _ ->
                        _uiState.update {
                            it.copy(
                                errorMessage = message,
                                isLoading = false,
                                isSuccess = false
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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val apiService = RetrofitClient.chatApiService
                val remoteDataSource = ConversationListRemoteDataSourceImpl(apiService)
                val repository = ConversationListRepositoryImpl(
                    remoteDataSource = remoteDataSource
                )
                val getConversationListUseCase = GetConversationListUseCase(
                    repository = repository
                )

                return ConversationListViewModel(
                    getConversationListUseCase
                ) as T
            }
        }
    }
}