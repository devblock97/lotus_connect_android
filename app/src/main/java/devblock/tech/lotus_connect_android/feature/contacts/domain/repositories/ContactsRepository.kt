package devblock.tech.lotus_connect_android.feature.contacts.domain.repositories

import devblock.tech.lotus_connect_android.core.model.BaseResponseModel
import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend

interface ContactsRepository {
    suspend fun getFriendsList(): Result<List<Friend>>

    suspend fun sendFriendRequest(username: String): Result<BaseResponseModel>

    suspend fun acceptFriend(friendId: String): Result<BaseResponseModel>

    suspend fun rejectFriend(friendId: String): Result<BaseResponseModel>

    suspend fun deleteFriend(friendId: String): Result<BaseResponseModel>

    suspend fun getRequestersList(): Result<List<Friend>>
}