package devblock.tech.lotus_connect_android.feature.notifications.domain.usecase

import devblock.tech.lotus_connect_android.feature.notifications.data.dto.NotificationResponseDto
import devblock.tech.lotus_connect_android.feature.notifications.domain.repositories.NotificationRepository

data class DeleteNotificationParam(
    val notificationId: String
)
class DeleteNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(param: DeleteNotificationParam) : Result<NotificationResponseDto> {
        return repository.deleteNotification(param.notificationId)
    }
}