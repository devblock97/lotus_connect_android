package devblock.tech.lotus_connect_android.feature.settings.data.datasources

import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User
import devblock.tech.lotus_connect_android.feature.settings.data.service.ProfileService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

interface ProfileRemoteDataSource {
    suspend fun uploadAvatar(file: File): User
}

class ProfileRemoteDataSourceImpl(
    val profileService: ProfileService
) : ProfileRemoteDataSource {

    override suspend fun uploadAvatar(file: File): User {
        if (!file.exists()) {
            throw IllegalArgumentException("Avatar file not found at path: ${file.path}")
        }

        val mediaType = when (file.extension.lowercase()) {
            "png" -> "image/png"
            "webp" -> "image/webp"
            else -> "image/jpeg"
        }.toMediaTypeOrNull()

        val requestBody = file.asRequestBody(mediaType)
        val part = MultipartBody.Part.createFormData(
            name = "avatar",
            filename = file.name,
            body = requestBody
        )

        val response =  profileService.uploadAvatar(part)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to upload avatar")
        }
    }

}