package devblock.tech.lotus_connect_android.feature.auth.presentation.view_model

import devblock.tech.lotus_connect_android.core.exception.AppException
import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User

data class AuthUiState(
    val isCheckingSession: Boolean = true,
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val error: AppException? = null
)