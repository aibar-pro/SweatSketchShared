package pro.aibar.sweatsketch.shared.data.repository

import pro.aibar.sweatsketch.shared.data.api.ApiException
import pro.aibar.sweatsketch.shared.data.model.AuthTokenModel
import pro.aibar.sweatsketch.shared.data.model.RefreshTokenModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel

interface AuthRepository {
    @Throws(ApiException::class, Exception::class)
    suspend fun login(userCredential: UserCredentialModel): AuthTokenModel
    @Throws(ApiException::class, Exception::class)
    suspend fun refreshToken(refreshTokenModel: RefreshTokenModel): AuthTokenModel
}