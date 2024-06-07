package pro.aibar.sweatsketch.shared.data.repository

import pro.aibar.sweatsketch.shared.data.model.ResponseMessageModel
import pro.aibar.sweatsketch.shared.data.model.UserCredentialModel
import pro.aibar.sweatsketch.shared.data.model.UserProfileModel

interface UserRepository {
    suspend fun createUser(userCredentialModel: UserCredentialModel): ResponseMessageModel
    suspend fun createUserProfile(login: String, userProfile: UserProfileModel): ResponseMessageModel
    suspend fun getUserProfile(login: String): UserProfileModel
    suspend fun updateUserProfile(login: String, userProfile: UserProfileModel): ResponseMessageModel
}