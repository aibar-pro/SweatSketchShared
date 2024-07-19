package pro.aibar.sweatsketch.shared.data.repository

import io.ktor.http.HttpStatusCode
import pro.aibar.sweatsketch.shared.data.api.ApiException
import pro.aibar.sweatsketch.shared.data.api.UserApi
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.data.model.UserProfileModel

class UserRepositoryImpl(private val api: UserApi) : UserRepository {
    @Throws(ApiException::class, Exception::class)
    override suspend fun createUser(userCredential: UserCredentialModel): ResponseMessageModel {
        return try {
            api.createUser(userCredential)
        } catch (e: ApiException) {
            println("API exception: ${e.status} - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            throw e
        }
    }

    @Throws(ApiException::class, Exception::class)
    override suspend fun createUserProfile(userProfile: UserProfileModel): ResponseMessageModel {
        return try {
            api.createUserProfile(userProfile)
        } catch (e: ApiException) {
            println("API exception: ${e.status} - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            throw e
        }
    }

    @Throws(ApiException::class, Exception::class)
    override suspend fun getUserProfile(): UserProfileModel {
        return try {
            api.getUserProfile()
        } catch (e: ApiException) {
            if (e.status == HttpStatusCode.NotFound) {
                try {
                    api.createDefaultUserProfile()
                    api.getUserProfile()
                } catch (e: ApiException) {
                    println("API exception: ${e.status} - ${e.message}")
                    throw e
                } catch (e: Exception) {
                    println("Unexpected exception: ${e.message}")
                    throw e
                }
            } else {
                println("API exception: ${e.status} - ${e.message}")
                throw e
            }
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            throw e
        }
    }

    @Throws(ApiException::class, Exception::class)
    override suspend fun updateUserProfile(userProfile: UserProfileModel): ResponseMessageModel {
        return try {
            api.updateUserProfile(userProfile)
        } catch (e: ApiException) {
            println("API exception: ${e.status} - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            throw e
        }
    }
}
