package pro.aibar.sweatsketch.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileModel (
    val login: String,
    val username: String?,
    val age: Int?,
    val height: Double?,
    val weight: Double?
)
