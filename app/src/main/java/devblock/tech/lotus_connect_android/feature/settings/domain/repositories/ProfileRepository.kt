package devblock.tech.lotus_connect_android.feature.settings.domain.repositories

import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User
import java.io.File

interface ProfileRepository {
    suspend fun uploadAvatar(file: File): Result<User>
}