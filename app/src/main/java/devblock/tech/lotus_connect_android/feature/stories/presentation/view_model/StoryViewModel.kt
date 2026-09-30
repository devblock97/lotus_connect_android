package devblock.tech.lotus_connect_android.feature.stories.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.core.view_model.BaseViewModel
import devblock.tech.lotus_connect_android.feature.stories.data.datasources.StoryRemoteDataSource
import devblock.tech.lotus_connect_android.feature.stories.data.datasources.StoryRemoteDataSourceImpl
import devblock.tech.lotus_connect_android.feature.stories.data.dto.StoryDto
import devblock.tech.lotus_connect_android.feature.stories.data.repositories.StoryRepositoryImpl
import devblock.tech.lotus_connect_android.feature.stories.domain.repositories.StoryRepository
import devblock.tech.lotus_connect_android.feature.stories.domain.usecase.GetStoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.AutoCloseable

data class StoryUiState(
    val isLoading: Boolean = false,
    val stories: List<StoryDto> = emptyList(),
    val errorMessage: String? = null,
)

@Suppress("UNCHECKED_CAST")
class StoryViewModel(
    private val getStoryUseCase: GetStoryUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(StoryUiState())
    val uiState: StateFlow<StoryUiState> = _uiState.asStateFlow()

    init {
        getStories()
    }

    fun getStories() {
        viewModelScope.launch {
            val result = getStoryUseCase()
            result.fold(
                onSuccess = { stories ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            stories = stories,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val storyService = RetrofitClient.storyService
                val remoteDataSource = StoryRemoteDataSourceImpl(
                    storyService = storyService
                )
                val storyRepository = StoryRepositoryImpl(
                    remoteDataSource = remoteDataSource
                )
                val getStoryUseCase = GetStoryUseCase(
                    storyRepository = storyRepository
                )
                return StoryViewModel(
                    getStoryUseCase = getStoryUseCase
                ) as T
            }
        }
    }
}