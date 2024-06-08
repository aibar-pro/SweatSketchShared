package pro.aibar.sweatsketch.shared.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import pro.aibar.sweatsketch.shared.data.model.AuthTokenModel
import pro.aibar.sweatsketch.shared.data.model.RefreshTokenModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel

interface AuthApi {
    suspend fun login(userCredential: UserCredentialModel): AuthTokenModel
    suspend fun refreshToken(refreshTokenModel: RefreshTokenModel): AuthTokenModel
}

class AuthApiImpl(private val client: HttpClient, private val baseUrl: String) : AuthApi {
    override suspend fun login(userCredential: UserCredentialModel): AuthTokenModel {
        return try {
            val response: HttpResponse = client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(userCredential)
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

    override suspend fun refreshToken(refreshTokenModel: RefreshTokenModel): AuthTokenModel {
        return try {
            val response: HttpResponse = client.post("$baseUrl/auth/refresh-token") {
                contentType(ContentType.Application.Json)
                setBody(refreshTokenModel)
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