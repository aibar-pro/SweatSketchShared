package pro.aibar.sweatsketch.shared.data.repository

import pro.aibar.sweatsketch.shared.data.api.ApiException
import pro.aibar.sweatsketch.shared.data.model.AuthTokenDto
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialDto

interface AuthRepository {
    @Throws(ApiException::class, Exception::class)
    suspend fun login(userCredential: UserCredentialDto): AuthTokenDto
    @Throws(ApiException::class, Exception::class)
    suspend fun refreshToken(): AuthTokenDto
    suspend fun isLoggedIn(): Boolean
    @Throws(ApiException::class, Exception::class)
    suspend fun logout(): ResponseMessageModel
}
