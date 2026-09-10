package devblock.tech.lotus_connect_android.feature.notifications.domain.repositories

import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<NotificationEntity>>
    suspend fun markAsRead(notificationId: String): Boolean
    suspend fun deleteNotification(notificationId: String): Boolean
    suspend fun clearAllNotifications(): Boolean
}