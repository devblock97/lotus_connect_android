package devblock.tech.lotus_connect_android.feature.settings.domain.usecase

import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User
import devblock.tech.lotus_connect_android.feature.settings.domain.repositories.ProfileRepository
import java.io.File

data class UploadAvatarParam(
    val file: File
)

class UploadAvatarUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(param: UploadAvatarParam): Result<User> {
        return repository.uploadAvatar(param.file)
    }
}