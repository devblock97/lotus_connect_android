package devblock.tech.lotus_connect_android.feature.stories.domain.entities

import com.google.gson.annotations.SerializedName

data class StoryEntity(
    val id: String,
    @SerializedName("mediaType") val mediaType: String,
    @SerializedName("mediaUrl") val mediaUrl: String,
    val duration: Double,
    val visibility: String,
    @SerializedName("viewCount") val viewCount: Int,
    @SerializedName("hasViewer") val hasViewer: Boolean,
    @SerializedName("viewerReaction") val viewerReaction: String,
    @SerializedName("isCloseFriend") val isCloseFriend: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("expiredAt") val expiredAt: String,
)
