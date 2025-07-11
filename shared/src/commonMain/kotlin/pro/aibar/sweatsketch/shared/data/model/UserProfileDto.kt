package pro.aibar.sweatsketch.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val username: String? = null,
    val age: Int? = null,
    val height: Double? = null,
    val weight: Double? = null
)
