package pro.aibar.sweatsketch.shared.data.repository

import io.ktor.http.HttpStatusCode
import pro.aibar.sweatsketch.shared.data.api.ApiException
import pro.aibar.sweatsketch.shared.data.api.AuthApi
import pro.aibar.sweatsketch.shared.data.model.AuthTokenModel
import pro.aibar.sweatsketch.shared.data.model.RefreshTokenModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.util.TokenManager

class AuthRepositoryImpl(private val api: AuthApi) : AuthRepository {
    @Throws(ApiException::class, Exception::class)
    override suspend fun login(userCredential: UserCredentialModel): AuthTokenModel {
        return try {
            val token = api.login(userCredential)
            token
        } catch (e: ApiException) {
            println("API exception: ${e.status} - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            throw e
        }
    }

    @Throws(ApiException::class, Exception::class)
    override suspend fun refreshToken(refreshTokenModel: RefreshTokenModel): AuthTokenModel {
        val refreshToken = TokenManager.getRefreshToken() ?: throw ApiException(HttpStatusCode.Unauthorized, "No refresh token found in storage")
        return try {
            val token = api.refreshToken(RefreshTokenModel(refreshToken))
            token
        } catch (e: ApiException) {
            println("API exception: ${e.status} - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            throw e
        }
    }

    @Throws(ApiException::class, Exception::class)
    override suspend fun isLoggedIn(): Boolean {
        return try {
            api.getValidAccessToken() != null
        } catch (e: ApiException) {
            false
        }
    }
}
