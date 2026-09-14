package devblock.tech.lotus_connect_android.feature.contacts.domain.entities

data class Friend(
    val id: String,
    val username: String,
    val fullName: String,
    val email: String,
    val friendShipStatus: String?,
    val friendShipSenderId: String?,
)
