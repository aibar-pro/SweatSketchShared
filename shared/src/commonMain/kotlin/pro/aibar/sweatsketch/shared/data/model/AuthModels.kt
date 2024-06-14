package pro.aibar.sweatsketch.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenModel(val accessToken: String, val refreshToken: String, val expiresIn: ULong)

@Serializable
data class RefreshTokenModel(val refreshToken: String)

@Serializable
data class UserCredentialModel(val login: String, val password: String)

@Serializable
data class DeleteSessionRequest(val login: String)
