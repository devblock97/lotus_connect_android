package devblock.tech.lotus_connect_android.feature.contacts.domain.usecase

import devblock.tech.lotus_connect_android.core.model.BaseResponseModel
import devblock.tech.lotus_connect_android.feature.contacts.domain.repositories.ContactsRepository

data class SendFriendRequestParam(
    val username: String
)

class SendFriendRequestUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(param: SendFriendRequestParam): Result<BaseResponseModel> {
        return repository.sendFriendRequest(param.username)
    }
}
