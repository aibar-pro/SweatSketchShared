package pro.aibar.sweatsketch.shared.util

import platform.Foundation.NSUserDefaults

actual object SecureStorage {
    private const val refreshTokenKey = "refreshToken"

    actual fun saveRefreshToken(token: String) {
        // TODO: Implement Keychain
        val userDefaults = NSUserDefaults.standardUserDefaults
        userDefaults.setObject(token, forKey = refreshTokenKey)
    }

    actual fun getRefreshToken(): String? {
        val userDefaults = NSUserDefaults.standardUserDefaults
        return userDefaults.stringForKey(refreshTokenKey)
    }

    actual fun clearRefreshToken() {
        val userDefaults = NSUserDefaults.standardUserDefaults
        userDefaults.removeObjectForKey(refreshTokenKey)
    }
}
