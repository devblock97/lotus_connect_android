package devblock.tech.lotus_connect_android.feature.notifications.presentation.view_model

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.notifications.data.datasources.NotificationRemoteDataSource
import devblock.tech.lotus_connect_android.feature.notifications.data.repositories.NotificationRepositoryImpl
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.DeleteAllNotificationsUseCase
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.DeleteNotificationParam
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.DeleteNotificationUseCase
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.GetNotificationsUseCase
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.ReadAllNotificationsUseCase
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.ReadNotificationParam
import devblock.tech.lotus_connect_android.feature.notifications.domain.usecase.ReadNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
    private val readAllNotificationsUseCase: ReadAllNotificationsUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase,
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
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load notifications"
                        )
                    }
                }
            )
        }
    }

    fun readNotification(notificationId: String) {
        viewModelScope.launch {
            val result = readNotificationUseCase(ReadNotificationParam(notificationId))

            result.fold(
                onSuccess = { data ->
                    val updated = _uiState.value.notifications.map { item ->
                        if (item.id == notificationId) item.copy(isRead = true) else item
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            notifications = updated
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to read notification"
                        )
                    }
                }
            )
        }
    }

    fun readAllNotification() {
        viewModelScope.launch {
            val result = readAllNotificationsUseCase()

            result.fold(
                onSuccess = { data ->
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = true)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to read all notification"
                        )
                    }
                }
            )
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = deleteNotificationUseCase(DeleteNotificationParam(notificationId))

            result.fold(
                onSuccess = {
                    println("delete notification success")
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = true)
                    }
                    getNotifications()
                },
                onFailure = { error ->
                    println("delete notification error")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to delete notification"
                        )
                    }
                }
            )
        }
    }

    fun deleteAllNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = deleteAllNotificationsUseCase()

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = true)
                    }
                    getNotifications()
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to delete all notifications"
                        )
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
                val readNotificationUseCase = ReadNotificationUseCase(repository)
                val readAllNotificationsUseCase = ReadAllNotificationsUseCase(repository)
                val deleteNotificationUseCase = DeleteNotificationUseCase(repository)
                val deleteAllNotificationsUseCase = DeleteAllNotificationsUseCase(repository)

                return NotificationViewModel(
                    getNotificationsUseCase = getNotificationsUseCase,
                    readNotificationUseCase = readNotificationUseCase,
                    readAllNotificationsUseCase = readAllNotificationsUseCase,
                    deleteNotificationUseCase = deleteNotificationUseCase,
                    deleteAllNotificationsUseCase = deleteAllNotificationsUseCase
                ) as T
            }
        }
    }
}