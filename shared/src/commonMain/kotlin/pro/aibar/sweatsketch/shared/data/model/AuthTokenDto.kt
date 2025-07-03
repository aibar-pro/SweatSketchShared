package pro.aibar.sweatsketch.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenDto(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: ULong
)

