package pro.aibar.sweatsketch.shared.data.repository

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
    override suspend fun getUserProfile(login: String): UserProfileModel {
        return try {
            api.getUserProfile(login)
        } catch (e: ApiException) {
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
