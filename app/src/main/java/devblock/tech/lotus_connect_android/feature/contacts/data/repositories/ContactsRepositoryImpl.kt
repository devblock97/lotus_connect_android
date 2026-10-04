package devblock.tech.lotus_connect_android.feature.contacts.data.repositories

import devblock.tech.lotus_connect_android.core.model.BaseResponseModel
import devblock.tech.lotus_connect_android.feature.contacts.data.datasources.ContactsRemoteDataSource
import devblock.tech.lotus_connect_android.feature.contacts.data.models.AcceptFriendRequest
import devblock.tech.lotus_connect_android.feature.contacts.data.models.RejectFriendRequest
import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend
import devblock.tech.lotus_connect_android.feature.contacts.domain.repositories.ContactsRepository

class ContactsRepositoryImpl(
    private val remoteDataSource: ContactsRemoteDataSource
): ContactsRepository {

    override suspend fun getFriendsList(): Result<List<Friend>> = runCatching {
        val response = remoteDataSource.getFriendsList()
        response
    }

    override suspend fun sendFriendRequest(username: String): Result<BaseResponseModel> = runCatching {
        remoteDataSource.sendFriendRequest(username)
    }

    override suspend fun acceptFriend(friendId: String): Result<BaseResponseModel> = runCatching {
        val response = remoteDataSource.acceptFriend(
            acceptFriendRequest = AcceptFriendRequest(friendId = friendId)
        )
        response
    }

    override suspend fun rejectFriend(friendId: String): Result<BaseResponseModel> = runCatching {
        val response = remoteDataSource.rejectFriend(
            rejectFriendRequest = RejectFriendRequest(
                friendId = friendId
            )
        )
        response
    }

    override suspend fun deleteFriend(friendId: String): Result<BaseResponseModel> = runCatching {
        val response = remoteDataSource.deleteFriend(friendId)
        response
    }

    override suspend fun getRequestersList(): Result<List<Friend>> = runCatching {
        val response = remoteDataSource.getRequestersList()
        response
    }
}