package devblock.tech.lotus_connect_android.feature.settings.data.service

import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfileService {

    @Multipart
    @POST("users/avatar")
    suspend fun uploadAvatar(@Part avatar: MultipartBody.Part): Response<User>
}