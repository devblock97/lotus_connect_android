package devblock.tech.lotus_connect_android.feature.notifications.data.service

import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity
import retrofit2.Response
import retrofit2.http.GET

interface NotificationService {
    @GET("users/notifications")
    suspend fun getNotifications(): Response<List<NotificationEntity>>
}