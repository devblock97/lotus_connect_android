package devblock.tech.lotus_connect_android.feature.notifications.data.repositories

import devblock.tech.lotus_connect_android.feature.notifications.data.datasources.NotificationRemoteDataSource
import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity
import devblock.tech.lotus_connect_android.feature.notifications.domain.repositories.NotificationRepository

class NotificationRepositoryImpl(
    private val remoteDataSource: NotificationRemoteDataSource
): NotificationRepository {

    override suspend fun getNotifications(): Result<List<NotificationEntity>> = runCatching {
        val response = remoteDataSource.getNotifications()
        response
    }

    override suspend fun markAsRead(notificationId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotification(notificationId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun clearAllNotifications(): Boolean {
        TODO("Not yet implemented")
    }

}