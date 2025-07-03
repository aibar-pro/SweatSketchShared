package pro.aibar.sweatsketch.shared.util

expect object KeyStorage {
    suspend fun getDeviceId(): String
    suspend fun saveUserId(userId: String)
    suspend fun getUserId(): String?
    suspend fun clearUserId()
}