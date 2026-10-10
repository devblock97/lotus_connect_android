package devblock.tech.lotus_connect_android.feature.chat.presentation.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.gson.Gson
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.auth.data.datasources.AuthLocalDataSource
import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ChatRemoteDataSourceImpl
import devblock.tech.lotus_connect_android.feature.chat.data.repositories.ChatRepositoryImpl
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.usecase.GetMessagesHistoryParam
import devblock.tech.lotus_connect_android.feature.chat.domain.usecase.GetMessagesHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import devblock.tech.lotus_connect_android.core.view_model.BaseViewModel
import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ChatWebSocketDataSource
import devblock.tech.lotus_connect_android.feature.chat.data.datasources.ChatWebSocketDataSourceImpl
import devblock.tech.lotus_connect_android.feature.chat.domain.usecase.ObserveIncomingMessageUseCase
import devblock.tech.lotus_connect_android.feature.chat.domain.usecase.SendWebSocketMessageUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlin.time.Duration.Companion.milliseconds

@Suppress("UNCHECKED_CAST")
class ChatViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMessagesHistoryUseCase: GetMessagesHistoryUseCase,
    private val observeIncomingMessageUseCase: ObserveIncomingMessageUseCase,
    private val sendWebSocketMessageUseCase: SendWebSocketMessageUseCase,
    currentUserId: String?
): BaseViewModel() {

    private val conversationId: String = checkNotNull(savedStateHandle["conversationId"])
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private var realtimeJob: Job? = null
    private val gson = Gson()

    init {
        _uiState.update { it.copy(currentUserId = currentUserId) }
        getMessages(conversationId)
        observeRealtimeMessages(conversationId)
    }

    private fun getMessages(conversationId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
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
                        handleException(error, defaultMessage = "Failed to load messages") { message, _ ->
                            _uiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    errorMessage = message
                                )
                            }
                        }
                    }
                )

            } catch (e: Exception) {
                handleException(e, defaultMessage = "Failed to load messages") { message, _ ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            }
        }
    }

    private fun observeRealtimeMessages(conversationId: String) {
        realtimeJob?.cancel()
        realtimeJob = viewModelScope.launch {
            observeIncomingMessageUseCase(conversationId)
                .retryWhen { cause, attempt ->
                    if (attempt < 5) {
                        val delayMs = (1000L * (1L shl attempt.toInt())).coerceAtMost(16000L)
                        delay(delayMs.milliseconds)
                        true
                    } else {
                        false
                    }
                }
                .catch { error ->
                    handleException(error, defaultMessage = "Real-time connect lost") { message, _ ->
                        _uiState.update { it.copy(errorMessage = message) }
                    }
                }
                .collect { newMessage ->
                    _uiState.update { state ->
                        val alreadyExists = state.messages.any { it.id == newMessage.id }
                        if (alreadyExists) {
                            state
                        } else {
                            val pendingIndex = state.messages.indexOfFirst {
                                it.id.startsWith("temp_") &&
                                    it.senderId == newMessage.senderId &&
                                    it.content == newMessage.content
                            }
                            if (pendingIndex != -1) {
                                val updatedList = state.messages.toMutableList()
                                updatedList[pendingIndex] = newMessage
                                state.copy(messages = updatedList)
                            } else {
                                state.copy(messages = state.messages + newMessage)
                            }
                        }
                    }
                }
        }
    }

    fun sendMessage(content: String) {
        val trimmedContent = content.trim()
        if (trimmedContent.isEmpty()) return

        val tempId = "temp_${System.currentTimeMillis()}"
        val senderId = uiState.value.currentUserId ?: ""
        val nowIso = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }.format(java.util.Date())

        val optimisticMessage = MessageEntity(
            id = tempId,
            content = trimmedContent,
            senderId = senderId,
            createdAt = nowIso,
            conversationId = conversationId
        )

        // Optimistically insert message into UI for instant feedback
        _uiState.update { state ->
            state.copy(
                isSending = true,
                messages = state.messages + optimisticMessage
            )
        }

        viewModelScope.launch {
            val result = sendWebSocketMessageUseCase(conversationId, trimmedContent)
            result.fold(
                onSuccess = { confirmedMessage ->
                    _uiState.update { state ->
                        if (!confirmedMessage.id.startsWith("temp_")) {
                            val updatedList = state.messages.map { msg ->
                                if (msg.id == tempId) confirmedMessage else msg
                            }
                            state.copy(isSending = false, messages = updatedList)
                        } else {
                            state.copy(isSending = false)
                        }
                    }
                },
                onFailure = { error ->
                    handleException(error, defaultMessage = "Unable to send message. Please check connection.") { message, _ ->
                        _uiState.update { state ->
                            state.copy(
                                isSending = false,
                                messages = state.messages.filterNot { it.id == tempId },
                                errorMessage = message
                            )
                        }
                    }
                }
            )
        }
    }

    fun reconnectRealtime() {
        observeRealtimeMessages(conversationId)
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    override fun onCleared() {
        super.onCleared()
        realtimeJob?.cancel()
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

                val webSocketDataSource = ChatWebSocketDataSourceImpl(client = RetrofitClient.okHttpClient)

                val repository = ChatRepositoryImpl(
                    remoteDataSource = remoteDataSource,
                    authLocalDataSource = localDataSource,
                    chatWebSocketDataSource = webSocketDataSource
                )
                val getMessagesHistoryUseCase = GetMessagesHistoryUseCase(
                    repository = repository
                )
                val observeIncomingMessageUseCase = ObserveIncomingMessageUseCase(repository = repository)
                val sendWebSocketMessageUseCase = SendWebSocketMessageUseCase(repository = repository)

                return ChatViewModel(
                    savedStateHandle = savedStateHandle,
                    getMessagesHistoryUseCase = getMessagesHistoryUseCase,
                    observeIncomingMessageUseCase = observeIncomingMessageUseCase,
                    sendWebSocketMessageUseCase = sendWebSocketMessageUseCase,
                    currentUserId = currentUserId
                ) as T
            }
        }
    }
}