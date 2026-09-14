package devblock.tech.lotus_connect_android.feature.contacts.domain.repositories

import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend

interface ContactsRepository {
    suspend fun getFriendsList(): Result<List<Friend>>
}