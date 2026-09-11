package devblock.tech.lotus_connect_android.feature.notifications.data.repositories

import devblock.tech.lotus_connect_android.feature.notifications.data.datasources.NotificationRemoteDataSource
import devblock.tech.lotus_connect_android.feature.notifications.data.dto.NotificationResponseDto
import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity
import devblock.tech.lotus_connect_android.feature.notifications.domain.repositories.NotificationRepository

class NotificationRepositoryImpl(
    private val remoteDataSource: NotificationRemoteDataSource
): NotificationRepository {

    override suspend fun getNotifications(): Result<List<NotificationEntity>>
    = runCatching {
        val response = remoteDataSource.getNotifications()
        response
    }

    override suspend fun markAsRead(notificationId: String): Result<NotificationResponseDto>
    = runCatching {
        val response = remoteDataSource.readNotification(notificationId)
        response
    }

    override suspend fun deleteNotification(notificationId: String): Result<NotificationResponseDto>
    = runCatching {
        val response = remoteDataSource.deleteNotification(notificationId)
        response
    }

    override suspend fun readAllNotifications(): Result<NotificationResponseDto>
    = runCatching {
        val response = remoteDataSource.readAllNotifications()
        response
    }

    override suspend fun deleteAllNotifications(): Result<NotificationResponseDto>
    = runCatching {
        val response = remoteDataSource.deleteAllNotifications()
        response
    }

}