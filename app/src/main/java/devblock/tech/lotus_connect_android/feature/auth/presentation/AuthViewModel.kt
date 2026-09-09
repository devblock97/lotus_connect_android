package devblock.tech.lotus_connect_android.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.auth.data.local.AuthLocalDataSource
import devblock.tech.lotus_connect_android.feature.auth.data.remote.AuthRemoteDataSource
import devblock.tech.lotus_connect_android.feature.auth.data.repositories.AuthRepositoryImpl
import devblock.tech.lotus_connect_android.feature.auth.domain.usecase.LoginParam
import devblock.tech.lotus_connect_android.feature.auth.domain.usecase.LoginUseCase
import devblock.tech.lotus_connect_android.feature.auth.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        println("trigger login use case")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = loginUseCase(LoginParam(email = email, password = password))

            result.fold(
                onSuccess = { user ->
                    println("check login success: ${user.fullName}")
                    _uiState.update {
                        it.copy(isLoading = false, user = user, isSuccess = true)
                    }
                },
                onFailure = { error ->
                    println("check login error: ${error.message}")
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Authentication failed")
                    }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            logoutUseCase()
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun<T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                val apiService = RetrofitClient.authApiService
                val remoteDataSource = AuthRemoteDataSource(apiService)
                val localDataSource = AuthLocalDataSource(context = application.applicationContext)
                val repository = AuthRepositoryImpl(
                    remoteDataSource = remoteDataSource,
                    localDataSource = localDataSource
                )
                val loginUseCase = LoginUseCase(repository)
                val logoutUseCase = LogoutUseCase(repository)
                return AuthViewModel(
                    loginUseCase = loginUseCase,
                    logoutUseCase = logoutUseCase
                ) as T
            }
        }
    }
}

