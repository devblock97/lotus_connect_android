package devblock.tech.lotus_connect_android.feature.settings.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User
import devblock.tech.lotus_connect_android.feature.settings.data.datasources.ProfileRemoteDataSourceImpl
import devblock.tech.lotus_connect_android.feature.settings.data.repositories.ProfileRepositoryImpl
import devblock.tech.lotus_connect_android.feature.settings.domain.usecase.UploadAvatarParam
import devblock.tech.lotus_connect_android.feature.settings.domain.usecase.UploadAvatarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class ProfileUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val user: User? = null
)

class ProfileViewModel(
    private val uploadAvatarUseCase: UploadAvatarUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun uploadAvatar(file: File) {

        viewModelScope.launch {
            val param = UploadAvatarParam(file)
            val result = uploadAvatarUseCase(param)

            result.fold(
                onSuccess = { data ->
                    _uiState.update { state ->
                        state.copy(
                            isSuccess = true,
                            isLoading = false,
                            user = data
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

    fun resetState() {
        _uiState.update { state ->
            state.copy(isLoading = false)
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val profileService = RetrofitClient.profileService
                val remoteDataSource = ProfileRemoteDataSourceImpl(
                    profileService = profileService
                )
                val repository = ProfileRepositoryImpl(
                    remoteDataSource = remoteDataSource
                )
                val uploadAvatarUseCase = UploadAvatarUseCase(
                    repository = repository
                )
                return ProfileViewModel(
                    uploadAvatarUseCase = uploadAvatarUseCase
                ) as T
            }
        }
    }
}