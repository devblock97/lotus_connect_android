package devblock.tech.lotus_connect_android.feature.settings.data.repositories

import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User
import devblock.tech.lotus_connect_android.feature.settings.data.datasources.ProfileRemoteDataSource
import devblock.tech.lotus_connect_android.feature.settings.domain.repositories.ProfileRepository
import java.io.File

class ProfileRepositoryImpl(
    val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {

    override suspend fun uploadAvatar(file: File): Result<User> = runCatching {
        remoteDataSource.uploadAvatar(file)
    }
}