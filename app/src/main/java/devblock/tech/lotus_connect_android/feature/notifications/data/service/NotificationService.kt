package devblock.tech.lotus_connect_android.feature.notifications.data.service

import devblock.tech.lotus_connect_android.feature.notifications.data.dto.NotificationResponseDto
import devblock.tech.lotus_connect_android.feature.notifications.domain.entities.NotificationEntity
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationService {
    @GET("users/notifications")
    suspend fun getNotifications(): Response<List<NotificationEntity>>

    @POST("users/notifications/{notificationId}/read")
    suspend fun readNotification(@Path("notificationId") notificationId: String): Response<NotificationResponseDto>

    @DELETE("users/notifications/{notificationId}")
    suspend fun deleteNotification(@Path("notificationId") notificationId: String): Response<NotificationResponseDto>

    @POST("users/notifications/read-all")
    suspend fun readAllNotifications(): Response<NotificationResponseDto>

    @DELETE("users/notifications")
    suspend fun deleteAllNotifications(): Response<NotificationResponseDto>
}