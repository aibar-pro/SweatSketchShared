package pro.aibar.sweatsketch.shared.data.repository

import pro.aibar.sweatsketch.shared.data.api.ApiException
import pro.aibar.sweatsketch.shared.data.api.UserApi
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialDto
import pro.aibar.sweatsketch.shared.data.model.UserProfileDto

class UserRepositoryImpl(private val api: UserApi) : UserRepository {
    @Throws(ApiException::class, Exception::class)
    override suspend fun createUser(userCredential: UserCredentialDto): ResponseMessageModel {
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
    override suspend fun createUserProfile(userProfile: UserProfileDto): ResponseMessageModel {
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
    override suspend fun getUserProfile(): UserProfileDto {
        return try {
            api.getUserProfile()
        } catch (e: ApiException) {
            println("API exception: ${e.status} - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("Unexpected exception: ${e.message}")
            throw e
        }
    }

    @Throws(ApiException::class, Exception::class)
    override suspend fun updateUserProfile(userProfile: UserProfileDto): ResponseMessageModel {
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
