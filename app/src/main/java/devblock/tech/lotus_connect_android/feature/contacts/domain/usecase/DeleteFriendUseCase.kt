package devblock.tech.lotus_connect_android.feature.contacts.domain.usecase

import devblock.tech.lotus_connect_android.core.model.BaseResponseModel
import devblock.tech.lotus_connect_android.feature.contacts.domain.repositories.ContactsRepository

data class DeleteFriendParam(
    val friendId: String
)

class DeleteFriendUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(param: DeleteFriendParam): Result<BaseResponseModel> {
        return repository.deleteFriend(param.friendId)
    }
}