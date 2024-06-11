package pro.aibar.sweatsketch.shared.util

expect object SecureStorage {
    fun saveRefreshToken(token: String)
    fun getRefreshToken(): String?
    fun clearRefreshToken()
}
