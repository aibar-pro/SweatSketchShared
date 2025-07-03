package pro.aibar.sweatsketch.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentialDto(
    val login: String,
    val password: String
)