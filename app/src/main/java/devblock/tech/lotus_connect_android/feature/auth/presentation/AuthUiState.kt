package devblock.tech.lotus_connect_android.feature.auth.presentation

import devblock.tech.lotus_connect_android.feature.auth.domain.model.User

data class AuthUiState(
    val isCheckingSession: Boolean = true,
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)