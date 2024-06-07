package pro.aibar.sweatsketch.shared.util

object TokenManager {
    private var accessToken: String? = null
    private var accessTokenExpiresAt: ULong = 0u

    fun saveAccessToken(token: String, expiresIn: ULong) {
        accessToken = token
        accessTokenExpiresAt = expiresIn
    }

    fun getAccessToken(): String? {
        return accessToken
    }

    fun isAccessTokenExpired(): Boolean {
        return Platform.currentTimeMillis() >= accessTokenExpiresAt
    }

    fun saveRefreshToken(token: String) {
        SecureStorage.saveRefreshToken(token)
    }

    fun getRefreshToken(): String? {
        return SecureStorage.getRefreshToken()
    }
}