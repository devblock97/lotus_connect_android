package devblock.tech.lotus_connect_android.feature.notifications.domain.usecase

import devblock.tech.lotus_connect_android.feature.notifications.data.dto.NotificationResponseDto
import devblock.tech.lotus_connect_android.feature.notifications.domain.repositories.NotificationRepository

class DeleteAllNotificationsUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Result<NotificationResponseDto> {
        return repository.deleteAllNotifications()
    }
}