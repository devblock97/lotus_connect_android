package devblock.tech.lotus_connect_android.feature.contacts.data.repositories

import devblock.tech.lotus_connect_android.feature.contacts.data.datasources.ContactsRemoteDataSource
import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend
import devblock.tech.lotus_connect_android.feature.contacts.domain.repositories.ContactsRepository

class ContactsRepositoryImpl(
    private val remoteDataSource: ContactsRemoteDataSource
): ContactsRepository {

    override suspend fun getFriendsList(): Result<List<Friend>> = runCatching {
        val response = remoteDataSource.getFriendsList()
        response
    }
}