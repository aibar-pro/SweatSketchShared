package pro.aibar.sweatsketch.shared.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import pro.aibar.sweatsketch.shared.data.model.AuthTokenModel
import pro.aibar.sweatsketch.shared.data.model.DeleteSessionRequest
import pro.aibar.sweatsketch.shared.data.model.RefreshTokenModel
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.util.KeyStorage
import pro.aibar.sweatsketch.shared.util.SecureStorage
import pro.aibar.sweatsketch.shared.util.TokenManager

interface AuthApi {
    suspend fun login(userCredential: UserCredentialModel): AuthTokenModel
    suspend fun refreshToken(): AuthTokenModel
    suspend fun getValidAccessToken(): String
    suspend fun logout(): ResponseMessageModel
}

class AuthApiImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val tokenManager: TokenManager
) : AuthApi {
    override suspend fun getValidAccessToken(): String {
        if (tokenManager.isAccessTokenExpired() || tokenManager.getAccessToken() == null) {
            val newToken = refreshToken()
            saveToken(newToken)
        }
        return tokenManager.getAccessToken() ?: throw ApiException(HttpStatusCode.Unauthorized, "No access token found")
    }

    private fun saveToken(token: AuthTokenModel) {
        tokenManager.saveAccessToken(token.accessToken, token.expiresIn)
        tokenManager.saveRefreshToken(token.refreshToken)
    }

    override suspend fun login(userCredential: UserCredentialModel): AuthTokenModel {
        return try {
            val deviceId = KeyStorage.getDeviceId()
            val response: HttpResponse = client.post("$baseUrl/auth/login") {
                header("deviceId", deviceId)
                contentType(ContentType.Application.Json)
                setBody(userCredential)
            }
            if (response.status == HttpStatusCode.OK) {
                saveToken(response.body())
                KeyStorage.saveLogin(userCredential.login)
                response.body()
            } else {
                throw ApiException(response.status, response.bodyAsText())
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }

    override suspend fun refreshToken(): AuthTokenModel {
        val refreshToken = tokenManager.getRefreshToken() ?: throw ApiException(HttpStatusCode.Unauthorized, "No refresh token found in storage")
        return try {
            val response: HttpResponse = client.post("$baseUrl/auth/refresh-token") {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenModel(refreshToken))
            }
            if (response.status == HttpStatusCode.OK) {
                saveToken(response.body())
                response.body()
            } else {
                throw ApiException(response.status, response.bodyAsText())
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }

    override suspend fun logout(): ResponseMessageModel {
        return try {
            val login = KeyStorage.getLogin() ?: throw ApiException(HttpStatusCode.Unauthorized, "No login found")
            val deviceId = KeyStorage.getDeviceId()
            val response: HttpResponse = client.delete("$baseUrl/auth/sessions") {
                header("deviceId", deviceId)
                contentType(ContentType.Application.Json)
                setBody(DeleteSessionRequest(login))
            }
            if (response.status == HttpStatusCode.OK) {
                KeyStorage.clearLogin()
                SecureStorage.clearRefreshToken()
                response.body()
            } else {
                throw ApiException(response.status, response.bodyAsText())
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }
}
