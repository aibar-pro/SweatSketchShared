package pro.aibar.sweatsketch.shared.data.repository

import pro.aibar.sweatsketch.shared.data.api.ApiException
import pro.aibar.sweatsketch.shared.data.api.AuthApi
import pro.aibar.sweatsketch.shared.data.model.AuthTokenModel
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.util.KeyStorage

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
    override suspend fun refreshToken(): AuthTokenModel {
        return try {
            val token = api.refreshToken()
            token
        } catch (e: ApiException) {
            println("API exception: ${e.status} - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            throw e
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return try {
            api.getValidAccessToken() != null && KeyStorage.getLogin() != null
        } catch (e: ApiException) {
            println("API exception: ${e.status} - ${e.message}")
            false
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            false
        }
    }

    @Throws(ApiException::class, Exception::class)
    override suspend fun logout(): ResponseMessageModel {
        return try {
            val message = api.logout()
            message
        } catch (e: ApiException) {
            println("API exception: ${e.status} - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            throw e
        }
    }
}
