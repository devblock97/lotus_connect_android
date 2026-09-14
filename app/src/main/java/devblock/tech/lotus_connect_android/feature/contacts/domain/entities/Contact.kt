package devblock.tech.lotus_connect_android.feature.contacts.domain.entities

data class Contact(
    val id: String,
    val name: String,
    val role: String,
    val phone: String,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
    val isFavorite: Boolean = false
)

