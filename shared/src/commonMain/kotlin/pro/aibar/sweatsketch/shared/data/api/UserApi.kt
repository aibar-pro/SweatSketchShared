package pro.aibar.sweatsketch.shared.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import pro.aibar.sweatsketch.shared.data.model.RefreshTokenModel
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.data.model.UserProfileModel
import pro.aibar.sweatsketch.shared.util.TokenManager

interface UserApi {
    suspend fun createUser(userCredential: UserCredentialModel): ResponseMessageModel
    suspend fun createUserProfile(userProfile: UserProfileModel): ResponseMessageModel
    suspend fun getUserProfile(login: String): UserProfileModel
    suspend fun updateUserProfile(login: String, userProfile: UserProfileModel): ResponseMessageModel
}

class UserApiImpl(private val client: HttpClient, private val baseUrl: String, private val tokenManager: TokenManager, private val authApi: AuthApi) : UserApi {
    private suspend fun getValidAccessToken(): String {
        if (tokenManager.isAccessTokenExpired() || tokenManager.getAccessToken() != null) {
            val refreshToken = tokenManager.getRefreshToken() ?: throw ApiException(HttpStatusCode.Unauthorized, "No refresh token found. Login required")
            val newToken = authApi.refreshToken(RefreshTokenModel(refreshToken))
            tokenManager.saveAccessToken(newToken.accessToken, newToken.expiresIn)
            tokenManager.saveRefreshToken(newToken.refreshToken)
        }
        return tokenManager.getAccessToken() ?: throw ApiException(HttpStatusCode.Unauthorized, "No access token found")
    }
    override suspend fun createUser(userCredential: UserCredentialModel): ResponseMessageModel {
        return try {
            val response: HttpResponse = client.post("$baseUrl/user") {
                contentType(ContentType.Application.Json)
                setBody(userCredential)
            }
            if (response.status == HttpStatusCode.Created) {
                response.body()
            } else {
                throw ApiException(response.status, response.bodyAsText())
            }
        } catch (e: Exception) {
            throw ApiException(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }

    override suspend fun createUserProfile(userProfile: UserProfileModel): ResponseMessageModel {
        val accessToken = getValidAccessToken()
        return try {
            val response: HttpResponse = client.post("$baseUrl/user/profile") {
                header("Authorization", "Bearer $accessToken")
                contentType(ContentType.Application.Json)
                setBody(userProfile)
            }
            if (response.status == HttpStatusCode.Created) {
                response.body()
            } else {
                throw ApiException(response.status, response.bodyAsText())
            }
        } catch (e: Exception) {
            throw ApiException(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }

    override suspend fun getUserProfile(login: String): UserProfileModel {
        val accessToken = getValidAccessToken()
        return try {
            val response: HttpResponse = client.get("$baseUrl/user/profile/$login") {
                header("Authorization", "Bearer $accessToken")
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                response.body()
            } else {
                throw ApiException(response.status, response.bodyAsText())
            }
        } catch (e: Exception) {
            throw ApiException(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }

    override suspend fun updateUserProfile(login: String, userProfile: UserProfileModel): ResponseMessageModel {
        val accessToken = getValidAccessToken()
        return try {
            val response: HttpResponse = client.put("$baseUrl/user/profile/$login") {
                header("Authorization", "Bearer $accessToken")
                contentType(ContentType.Application.Json)
                setBody(userProfile)
            }
            if (response.status == HttpStatusCode.OK) {
                response.body()
            } else {
                throw ApiException(response.status, response.bodyAsText())
            }
        } catch (e: Exception) {
            throw ApiException(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }
}