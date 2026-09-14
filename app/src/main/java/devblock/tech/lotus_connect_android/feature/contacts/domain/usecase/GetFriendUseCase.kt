package devblock.tech.lotus_connect_android.feature.contacts.domain.usecase

import devblock.tech.lotus_connect_android.feature.contacts.domain.entities.Friend
import devblock.tech.lotus_connect_android.feature.contacts.domain.repositories.ContactsRepository

class GetFriendUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(): Result<List<Friend>> {
        return repository.getFriendsList()
    }
}