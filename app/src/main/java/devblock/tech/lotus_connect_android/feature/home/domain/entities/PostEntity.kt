package devblock.tech.lotus_connect_android.feature.home.domain.entities

import com.google.gson.annotations.SerializedName


data class PostEntity(
    val id: String,
    val author: Author,
    val content: String,
    @SerializedName("mediaItems") val mediaItems: List<MediaEntity> = emptyList(),
    val visibility: String,
    @SerializedName("likeCount") val likeCount: Int = 0,
    @SerializedName("commentCount") val commentCount: Int = 0,
    @SerializedName("userHasLiked") val userHasLiked: Boolean,
    @SerializedName("userReaction") val userReaction: List<String>,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
)

data class Author(
    val id: String,
    val username: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("avatarUrl") val avatarUrl: String?,
)

data class MediaEntity(
    val url: String,
)