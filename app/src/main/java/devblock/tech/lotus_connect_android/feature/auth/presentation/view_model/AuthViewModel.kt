package devblock.tech.lotus_connect_android.feature.auth.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.core.view_model.BaseViewModel
import devblock.tech.lotus_connect_android.feature.auth.data.datasources.AuthLocalDataSource
import devblock.tech.lotus_connect_android.feature.auth.data.datasources.AuthRemoteDataSource
import devblock.tech.lotus_connect_android.feature.auth.data.dto.RegisterRequest
import devblock.tech.lotus_connect_android.feature.auth.data.repositories.AuthRepositoryImpl
import devblock.tech.lotus_connect_android.feature.auth.domain.usecase.GetCachedUserUseCase
import devblock.tech.lotus_connect_android.feature.auth.domain.usecase.LoginParam
import devblock.tech.lotus_connect_android.feature.auth.domain.usecase.LoginUseCase
import devblock.tech.lotus_connect_android.feature.auth.domain.usecase.LogoutUseCase
import devblock.tech.lotus_connect_android.feature.auth.domain.usecase.RegisterParam
import devblock.tech.lotus_connect_android.feature.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getCachedUserUseCase: GetCachedUserUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState(isCheckingSession = true))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkSession()
    }

    fun checkSession() {
        viewModelScope.launch {
            try {
                val user = getCachedUserUseCase()
                if (user != null) {
                    _uiState.update {
                        it.copy(user = user, isSuccess = true, isCheckingSession = false)
                    }
                } else {
                    _uiState.update { it.copy(isCheckingSession = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isCheckingSession = false) }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, error = null) }

            val result = loginUseCase(LoginParam(email = email, password = password))

            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            isSuccess = true,
                            errorMessage = null,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    handleException(error, defaultMessage = "Authentication failed")
                }
            )
        }
    }

    fun register(params: RegisterRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, error = null) }

            val result = registerUseCase(RegisterParam(
                username = params.username,
                fullName = params.fullName,
                email = params.email,
                password = params.password
            ))

            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            isSuccess = true,
                            errorMessage = null,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    handleException(error, defaultMessage = "Registration failed")
                }
            )
        }
    }

    fun handleException(error: Throwable, defaultMessage: String = "Authentication failed") {
        super.handleException(error, defaultMessage) { message, appException ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = message,
                    error = appException
                )
            }
        }
    }

    fun logout(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            logoutUseCase()
            _uiState.value = AuthUiState(isCheckingSession = false)
            onComplete()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null, error = null) }
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
                val getCachedUserUseCase = GetCachedUserUseCase(repository)
                val registerUseCase = RegisterUseCase(repository)
                return AuthViewModel(
                    loginUseCase = loginUseCase,
                    logoutUseCase = logoutUseCase,
                    registerUseCase = registerUseCase,
                    getCachedUserUseCase = getCachedUserUseCase
                ) as T
            }
        }
    }
}

