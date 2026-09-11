package devblock.tech.lotus_connect_android.feature.notifications.domain.usecase

import devblock.tech.lotus_connect_android.feature.notifications.data.dto.NotificationResponseDto
import devblock.tech.lotus_connect_android.feature.notifications.domain.repositories.NotificationRepository

data class ReadNotificationParam(
    val notificationId: String,
)

class ReadNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(param: ReadNotificationParam): Result<NotificationResponseDto> {
        return repository.markAsRead(param.notificationId)
    }
}