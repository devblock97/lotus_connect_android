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
import kotlinx.coroutines.launch

class ConversationListViewModel(
    private val getConversationListUseCase: GetConversationListUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ConversationListUiState())
    val uiState: StateFlow<ConversationListUiState> = _uiState.asStateFlow()

    init {
        getConversationList()
    }

    fun getConversationList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = getConversationListUseCase()

            result.fold(
                onSuccess = { conversations ->
                    println("Display conversation list success: ${conversations.size}")
                    _uiState.value = _uiState.value.copy(
                        conversations = conversations,
                        isLoading = false,
                        isSuccess = true
                    )
                },
                onFailure = { error ->
                    println("Display conversation list error: ${error.message}")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = error.message,
                        isLoading = false,
                        isSuccess = false
                    )
                }
            )
        }
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