package devblock.tech.lotus_connect_android.feature.contacts.data.service

import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend
import retrofit2.Response
import retrofit2.http.GET

interface ContactsService {

    @GET("users/friends")
    suspend fun getFriendsList(): Response<List<Friend>>
}