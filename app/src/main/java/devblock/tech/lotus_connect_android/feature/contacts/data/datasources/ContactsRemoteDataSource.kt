package devblock.tech.lotus_connect_android.feature.contacts.data.datasources

import devblock.tech.lotus_connect_android.core.model.BaseResponseModel
import devblock.tech.lotus_connect_android.feature.contacts.data.models.AcceptFriendRequest
import devblock.tech.lotus_connect_android.feature.contacts.data.models.RejectFriendRequest
import devblock.tech.lotus_connect_android.feature.contacts.data.service.ContactsService
import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend

interface ContactsRemoteDataSource {
    suspend fun getFriendsList(): List<Friend>

    suspend fun sendFriendRequest(username: String): BaseResponseModel

    suspend fun acceptFriend(acceptFriendRequest: AcceptFriendRequest): BaseResponseModel

    suspend fun rejectFriend(rejectFriendRequest: RejectFriendRequest): BaseResponseModel

    suspend fun deleteFriend(friendId: String): BaseResponseModel

    suspend fun getRequestersList(): List<Friend>
}

class ContactsRemoteDataSourceImpl(
    val contactsService: ContactsService
) : ContactsRemoteDataSource {

    override suspend fun getFriendsList(): List<Friend> {
        val response = contactsService.getFriendsList()

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Failed to load friends list with HTTP code: ${response.code()}")
        }
    }

    override suspend fun sendFriendRequest(username: String): BaseResponseModel {
        val response = contactsService.sendFriendRequest(
            devblock.tech.lotus_connect_android.feature.contacts.data.models.SendFriendRequest(username = username)
        )

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Failed to send friend request")
        }
    }

    override suspend fun acceptFriend(acceptFriendRequest: AcceptFriendRequest): BaseResponseModel {
        val response = contactsService.acceptFriend(acceptFriendRequest)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Failed to accept friend")
        }
    }

    override suspend fun rejectFriend(rejectFriendRequest: RejectFriendRequest): BaseResponseModel {
        val response = contactsService.rejectFriend(rejectFriendRequest)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Failed to reject friend")
        }
    }

    override suspend fun deleteFriend(friendId: String): BaseResponseModel {
        val response = contactsService.deleteFriend(friendId)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Failed to delete friend")
        }
    }

    override suspend fun getRequestersList(): List<Friend> {
        val response = contactsService.getRequestersList()

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Failed to get requesters list")
        }
    }
}