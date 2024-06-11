package pro.aibar.sweatsketch.shared.data.repository

import pro.aibar.sweatsketch.shared.data.api.ApiException
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.data.model.UserProfileModel

interface UserRepository {
    @Throws(ApiException::class, Exception::class)
    suspend fun createUser(userCredential: UserCredentialModel): ResponseMessageModel
    @Throws(ApiException::class, Exception::class)
    suspend fun createUserProfile(userProfile: UserProfileModel): ResponseMessageModel
    @Throws(ApiException::class, Exception::class)
    suspend fun getUserProfile(login: String): UserProfileModel
    @Throws(ApiException::class, Exception::class)
    suspend fun updateUserProfile(login: String, userProfile: UserProfileModel): ResponseMessageModel
}
