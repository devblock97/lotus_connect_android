package devblock.tech.lotus_connect_android.feature.home.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.home.data.datasources.FeedRemoteDataSource
import devblock.tech.lotus_connect_android.feature.home.data.datasources.FeedRemoteDataSourceImpl
import devblock.tech.lotus_connect_android.feature.home.data.repositories.FeedRepositoryImpl
import devblock.tech.lotus_connect_android.feature.home.domain.entities.PostEntity
import devblock.tech.lotus_connect_android.feature.home.domain.repositories.FeedRepository
import devblock.tech.lotus_connect_android.feature.home.domain.usecases.GetFeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val feeds: List<PostEntity> = emptyList()
)

@Suppress("UNCHECKED_CAST")
class FeedViewModel(
    val getFeedUseCase: GetFeedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        getFeed()
    }

    fun getFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = getFeedUseCase()
                result.fold(
                    onFailure = { error ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                errorMessage = error.message
                            )
                        }
                    },
                    onSuccess = { feeds ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                feeds = feeds
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val feedService = RetrofitClient.feedApiService
                val remoteDataSource = FeedRemoteDataSourceImpl(
                    feedService = feedService
                )
                val repository = FeedRepositoryImpl(
                    remoteDataSource = remoteDataSource
                )
                val getFeedUseCase = GetFeedUseCase(repository = repository)
                return FeedViewModel(
                    getFeedUseCase = getFeedUseCase
                ) as T
            }
        }
    }
}