package devblock.tech.lotus_connect_android.feature.notifications.domain.repositories

import devblock.tech.lotus_connect_android.feature.notifications.data.dto.NotificationResponseDto
import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<NotificationEntity>>
    suspend fun markAsRead(notificationId: String): Result<NotificationResponseDto>
    suspend fun deleteNotification(notificationId: String): Result<NotificationResponseDto>
    suspend fun readAllNotifications(): Result<NotificationResponseDto>

    suspend fun deleteAllNotifications(): Result<NotificationResponseDto>
}