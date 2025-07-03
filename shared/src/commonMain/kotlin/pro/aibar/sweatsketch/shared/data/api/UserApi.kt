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
import pro.aibar.sweatsketch.shared.data.model.UserCredentialDto
import pro.aibar.sweatsketch.shared.data.model.UserProfileDto

interface UserApi {
    suspend fun createUser(userCredential: UserCredentialDto): ResponseMessageModel
    suspend fun createUserProfile(userProfile: UserProfileDto): ResponseMessageModel
    suspend fun getUserProfile(): UserProfileDto
    suspend fun updateUserProfile(userProfile: UserProfileDto): ResponseMessageModel
    suspend fun createDefaultUserProfile(login: String)
}

class UserApiImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val authApi: AuthApi
) : UserApi {
    override suspend fun createUser(userCredential: UserCredentialDto): ResponseMessageModel {
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

    override suspend fun createUserProfile(userProfile: UserProfileDto): ResponseMessageModel {
        return try {
            val accessToken = authApi.getValidAccessToken()
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

    override suspend fun createDefaultUserProfile(login: String) {
        val newUserProfile = UserProfileDto()
        try {
            createUserProfile(newUserProfile)
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUserProfile(): UserProfileDto {
        return try {
            val accessToken = authApi.getValidAccessToken()
            val response: HttpResponse = client.get("$baseUrl/user/profile") {
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

    override suspend fun updateUserProfile(userProfile: UserProfileDto): ResponseMessageModel {
        return try {
            val accessToken = authApi.getValidAccessToken()
            val response: HttpResponse = client.put("$baseUrl/user/profile") {
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
