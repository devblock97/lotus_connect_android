package devblock.tech.lotus_connect_android.feature.contacts.domain.usecase

import devblock.tech.lotus_connect_android.core.model.BaseResponseModel
import devblock.tech.lotus_connect_android.feature.contacts.domain.repositories.ContactsRepository

data class AcceptFriendParam(
    val friendId: String
)

class AcceptFriendUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(param: AcceptFriendParam): Result<BaseResponseModel> {
        return repository.acceptFriend(param.friendId)
    }
}