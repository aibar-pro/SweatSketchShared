import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.aibar.sweatsketch.shared.data.repository.AuthRepository
import pro.aibar.sweatsketch.shared.data.repository.UserRepository

class DIHelper : KoinComponent {
    val authRepository : AuthRepository by inject()
    val userRepository : UserRepository by inject()
}
