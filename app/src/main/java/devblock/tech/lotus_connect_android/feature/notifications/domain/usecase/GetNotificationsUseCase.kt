package devblock.tech.lotus_connect_android.feature.notifications.domain.usecase

import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity
import devblock.tech.lotus_connect_android.feature.notifications.domain.repositories.NotificationRepository

class GetNotificationsUseCase(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(): Result<List<NotificationEntity>> {
        return notificationRepository.getNotifications()
    }
}