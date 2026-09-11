package devblock.tech.lotus_connect_android.feature.notifications.data.dto

import com.google.gson.annotations.SerializedName

data class NotificationResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
)
