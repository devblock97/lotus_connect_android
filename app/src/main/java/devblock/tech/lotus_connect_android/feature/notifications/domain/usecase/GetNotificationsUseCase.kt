package devblock.tech.lotus_connect_android.feature.notifications.domain.usecase

import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity
import devblock.tech.lotus_connect_android.feature.notifications.domain.repositories.NotificationRepository

class GetNotificationsParam(
    val cursor: String?,
    val limit: Int
)
class GetNotificationsUseCase(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(param: GetNotificationsParam): Result<List<NotificationEntity>> {
        return notificationRepository.getNotifications(param.cursor, param.limit)
    }
}