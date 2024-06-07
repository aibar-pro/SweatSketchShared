package pro.aibar.sweatsketch.shared.data.repository

import pro.aibar.sweatsketch.shared.data.model.AuthTokenModel
import pro.aibar.sweatsketch.shared.data.model.RefreshTokenModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel

interface AuthRepository {
    suspend fun login(userCredential: UserCredentialModel): AuthTokenModel
    suspend fun refreshToken(refreshTokenModel: RefreshTokenModel): AuthTokenModel
}