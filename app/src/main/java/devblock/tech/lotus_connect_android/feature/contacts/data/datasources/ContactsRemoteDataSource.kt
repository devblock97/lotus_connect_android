package devblock.tech.lotus_connect_android.feature.contacts.data.datasources

import devblock.tech.lotus_connect_android.feature.contacts.data.service.ContactsService
import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend

class ContactsRemoteDataSource(
    val contactsService: ContactsService
) {

    suspend fun getFriendsList(): List<Friend> {
        val response = contactsService.getFriendsList()

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Failed to load friends list with HTTP code: ${response.code()}")
        }
    }
}