package devblock.tech.lotus_connect_android.feature.notifications.presentation.view_model

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.notifications.data.datasources.NotificationRemoteDataSource
import devblock.tech.lotus_connect_android.feature.notifications.data.repositories.NotificationRepositoryImpl
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.GetNotificationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    init {
        getNotifications()
    }
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    fun getNotifications() {
        viewModelScope.launch {
            val result = getNotificationsUseCase()

            result.fold(
                onSuccess = { notifications ->
                    println("notification success: ${notifications.size}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notifications = notifications,
                            errorMessage = null,
                            isSuccess = true
                        )
                    }
                },
                onFailure = { error ->
                    println("notification failure: ${error.message}")
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Failed to load notifications")
                    }
                }
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val apiService = RetrofitClient.notificationApiService
                val remoteDataSource = NotificationRemoteDataSource(apiService)
                val repository = NotificationRepositoryImpl(
                    remoteDataSource = remoteDataSource
                )
                val getNotificationsUseCase = GetNotificationsUseCase(repository)
                return NotificationViewModel(
                    getNotificationsUseCase = getNotificationsUseCase
                ) as T
            }
        }
    }
}