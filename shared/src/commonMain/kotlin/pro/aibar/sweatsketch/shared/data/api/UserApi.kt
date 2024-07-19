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
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.data.model.UserProfileModel
import pro.aibar.sweatsketch.shared.util.KeyStorage

interface UserApi {
    suspend fun createUser(userCredential: UserCredentialModel): ResponseMessageModel
    suspend fun createUserProfile(userProfile: UserProfileModel): ResponseMessageModel
    suspend fun getUserProfile(): UserProfileModel
    suspend fun updateUserProfile(userProfile: UserProfileModel): ResponseMessageModel
    suspend fun createDefaultUserProfile()
}

class UserApiImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val authApi: AuthApi
) : UserApi {
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
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }

    override suspend fun createUserProfile(userProfile: UserProfileModel): ResponseMessageModel {
        val accessToken = authApi.getValidAccessToken()
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
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }

    override suspend fun createDefaultUserProfile() {
        val login = KeyStorage.getLogin() ?: throw ApiException(HttpStatusCode.Unauthorized, "No login found")
        val newUserProfile = UserProfileModel(login)
        try {
            createUserProfile(newUserProfile)
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUserProfile(): UserProfileModel {
        val accessToken = authApi.getValidAccessToken()
        return try {
            val login = KeyStorage.getLogin() ?: throw ApiException(HttpStatusCode.Unauthorized, "No login found")
            val response: HttpResponse = client.get("$baseUrl/user/profile/$login") {
                header("Authorization", "Bearer $accessToken")
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
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

    override suspend fun updateUserProfile(userProfile: UserProfileModel): ResponseMessageModel {
        val accessToken = authApi.getValidAccessToken()
        return try {
            val login = KeyStorage.getLogin() ?: throw ApiException(HttpStatusCode.Unauthorized, "No login found")
            if (login != userProfile.login) {
                throw ApiException(HttpStatusCode.Unauthorized, "Profile login doesn't match authorized user")
            }
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
