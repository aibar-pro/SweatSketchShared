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
import pro.aibar.sweatsketch.shared.data.model.AuthTokenDto
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialDto
import pro.aibar.sweatsketch.shared.util.KeyStorage
import pro.aibar.sweatsketch.shared.util.SecureStorage
import pro.aibar.sweatsketch.shared.util.TokenManager

interface AuthApi {
    suspend fun login(userCredential: UserCredentialDto): AuthTokenDto
    suspend fun refreshToken(): AuthTokenDto
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
        return tokenManager.getAccessToken()
            ?: throw ApiException(HttpStatusCode.Unauthorized, "No access token found")
    }

    private fun saveToken(token: AuthTokenDto) {
        tokenManager.saveAccessToken(token.accessToken, token.expiresIn)
        tokenManager.saveRefreshToken(token.refreshToken)
    }

    override suspend fun login(userCredential: UserCredentialDto): AuthTokenDto {
        return try {
            val deviceId = KeyStorage.getDeviceId()
            val response: HttpResponse = client.post("$baseUrl/auth/login") {
                header("deviceId", deviceId)
                contentType(ContentType.Application.Json)
                setBody(userCredential)
            }
            if (response.status == HttpStatusCode.OK) {
                saveToken(response.body())
                KeyStorage.saveUserId(userCredential.login)
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

    override suspend fun refreshToken(): AuthTokenDto {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
                ?: throw ApiException(HttpStatusCode.Unauthorized, "No refresh token found in storage. Login required")

            val response: HttpResponse = client.post("$baseUrl/auth/refresh-token") {
                header("Authorization", "Bearer $refreshToken")
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
            val accessToken = getValidAccessToken()

            val response: HttpResponse = client.delete("$baseUrl/auth/sessions") {
                header("Authorization", "Bearer $accessToken")
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                KeyStorage.clearUserId()
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
