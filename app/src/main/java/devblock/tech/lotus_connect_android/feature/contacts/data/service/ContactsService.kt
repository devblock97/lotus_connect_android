package devblock.tech.lotus_connect_android.feature.contacts.data.service

import devblock.tech.lotus_connect_android.core.model.BaseResponseModel
import devblock.tech.lotus_connect_android.feature.contacts.data.models.AcceptFriendRequest
import devblock.tech.lotus_connect_android.feature.contacts.data.models.RejectFriendRequest
import devblock.tech.lotus_connect_android.feature.contacts.data.models.SendFriendRequest
import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ContactsService {

    @GET("users/friends")
    suspend fun getFriendsList(): Response<List<Friend>>

    @POST("users/friends")
    suspend fun sendFriendRequest(@Body sendFriendRequest: SendFriendRequest): Response<BaseResponseModel>

    @POST("users/friends/accept")
    suspend fun acceptFriend(@Body acceptFriend: AcceptFriendRequest): Response<BaseResponseModel>

    @POST("users/friends/reject")
    suspend fun rejectFriend(@Body rejectFriend: RejectFriendRequest): Response<BaseResponseModel>

    @DELETE("user/friend/{friendId}")
    suspend fun deleteFriend(@Path("friendId") friendId: String): Response<BaseResponseModel>

    @GET("users/friends/requests")
    suspend fun getRequestersList(): Response<List<Friend>>

}