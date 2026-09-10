package devblock.tech.lotus_connect_android.feature.auth.domain.entities

data class User(
    val id: String,
    val username: String,
    val email: String,
    val fullName: String? = null,
    val friendshipStatus: String? = null,
    val friendshipSenderId: String? = null,
)