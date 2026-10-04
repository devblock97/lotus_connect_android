package devblock.tech.lotus_connect_android.feature.contacts.domain.usecase

import devblock.tech.lotus_connect_android.core.model.BaseResponseModel
import devblock.tech.lotus_connect_android.feature.contacts.domain.repositories.ContactsRepository

data class RejectFriendParam(
    val friendId: String
)

class RejectFriendUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(param: RejectFriendParam): Result<BaseResponseModel> {
        return repository.rejectFriend(param.friendId)
    }
}