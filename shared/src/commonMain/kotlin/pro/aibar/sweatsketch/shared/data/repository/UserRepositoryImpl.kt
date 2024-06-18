package pro.aibar.sweatsketch.shared.data.repository

import io.ktor.http.HttpStatusCode
import pro.aibar.sweatsketch.shared.data.api.ApiException
import pro.aibar.sweatsketch.shared.data.api.UserApi
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.data.model.UserProfileModel
import pro.aibar.sweatsketch.shared.util.KeyStorage

class UserRepositoryImpl(private val api: UserApi) : UserRepository {
    @Throws(ApiException::class, Exception::class)
    override suspend fun createUser(userCredential: UserCredentialModel): ResponseMessageModel {
        return try {
            api.createUser(userCredential)
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    @Throws(ApiException::class, Exception::class)
    override suspend fun createUserProfile(userProfile: UserProfileModel): ResponseMessageModel {
        return try {
            api.createUserProfile(userProfile)
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
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
                    val login = KeyStorage.getLogin() ?: throw ApiException(HttpStatusCode.Unauthorized, "No login found")
                    val newUserProfile = UserProfileModel(login)
                    api.createUserProfile(newUserProfile)
                    api.getUserProfile()
                } catch (e: ApiException) {
                    throw e
                } catch (e: Exception) {
                    throw e
                }
            }
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    @Throws(ApiException::class, Exception::class)
    override suspend fun updateUserProfile(login: String, userProfile: UserProfileModel): ResponseMessageModel {
        return try {
            api.updateUserProfile(login, userProfile)
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }
}
