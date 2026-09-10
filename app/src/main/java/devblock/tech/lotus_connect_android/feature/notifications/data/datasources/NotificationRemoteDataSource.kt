package devblock.tech.lotus_connect_android.feature.notifications.data.datasources

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
}