package pro.aibar.sweatsketch.shared.data.repository

import pro.aibar.sweatsketch.shared.data.api.AuthApi
import pro.aibar.sweatsketch.shared.data.model.AuthTokenModel
import pro.aibar.sweatsketch.shared.data.model.RefreshTokenModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.util.TokenManager

class AuthRepositoryImpl(private val api: AuthApi) : AuthRepository {
    override suspend fun login(userCredential: UserCredentialModel): AuthTokenModel {
        val token = api.login(userCredential)
        TokenManager.saveAccessToken(token.accessToken, token.expiresIn)
        TokenManager.saveRefreshToken(token.refreshToken)
        return token
    }

    override suspend fun refreshToken(refreshTokenModel: RefreshTokenModel): AuthTokenModel {
        val token = api.refreshToken(refreshTokenModel)
        TokenManager.saveAccessToken(token.accessToken, token.expiresIn)
        TokenManager.saveRefreshToken(token.refreshToken)
        return token
    }


}