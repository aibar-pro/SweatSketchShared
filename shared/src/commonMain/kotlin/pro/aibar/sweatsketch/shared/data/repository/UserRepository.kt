package pro.aibar.sweatsketch.shared.data.repository

import pro.aibar.sweatsketch.shared.data.api.ApiException
import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialDto
import pro.aibar.sweatsketch.shared.data.model.UserProfileDto

interface UserRepository {
    @Throws(ApiException::class, Exception::class)
    suspend fun createUser(userCredential: UserCredentialDto): ResponseMessageModel
    @Throws(ApiException::class, Exception::class)
    suspend fun createUserProfile(userProfile: UserProfileDto): ResponseMessageModel
    @Throws(ApiException::class, Exception::class)
    suspend fun getUserProfile(): UserProfileDto
    @Throws(ApiException::class, Exception::class)
    suspend fun updateUserProfile(userProfile: UserProfileDto): ResponseMessageModel
}
