package devblock.tech.lotus_connect_android.feature.notifications.data.datasources

import devblock.tech.lotus_connect_android.feature.notifications.data.dto.NotificationResponseDto
import devblock.tech.lotus_connect_android.feature.notifications.data.service.NotificationService
import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity

class NotificationRemoteDataSource(
    private val notificationService: NotificationService
) {
    suspend fun getNotifications(): List<NotificationEntity> {
        val response = notificationService.getNotifications()

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Load notifications failed with HTTP ${response.code()}")
        }
    }

    suspend fun deleteNotification(notificationId: String): NotificationResponseDto {
        val response = notificationService.deleteNotification(notificationId)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Delete notification failed with HTTP ${response.code()}")
        }
    }

    suspend fun readNotification(notificationId: String): NotificationResponseDto {
        val response = notificationService.readNotification(notificationId)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Read notification failed with HTTP ${response.code()}")
        }
    }

    suspend fun readAllNotifications(): NotificationResponseDto {
        val response = notificationService.readAllNotifications()

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Read all notifications failed with HTTP ${response.code()}")
        }
    }

    suspend fun deleteAllNotifications(): NotificationResponseDto {
        val response = notificationService.deleteAllNotifications()

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Delete all notifications failed with HTTP ${response.code()}")
        }
    }
}