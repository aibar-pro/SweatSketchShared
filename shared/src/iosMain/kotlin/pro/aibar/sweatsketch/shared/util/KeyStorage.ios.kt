package pro.aibar.sweatsketch.shared.util

import platform.Foundation.NSUUID
import platform.Foundation.NSUserDefaults

actual object KeyStorage {
    private const val deviceIdKey = "deviceId"
    private const val loginKey = "login"

    actual fun getDeviceId(): String {
        val userDefaults = NSUserDefaults.standardUserDefaults
        var deviceId = userDefaults.stringForKey(deviceIdKey)
        if (deviceId == null) {
            deviceId = NSUUID.UUID().toString()
            userDefaults.setObject(deviceId, forKey = deviceIdKey)
        }
        return deviceId
    }

    actual fun saveLogin(login: String) {
        val userDefaults = NSUserDefaults.standardUserDefaults
        userDefaults.setObject(login, forKey = loginKey)
    }

    actual fun getLogin(): String? {
        val userDefaults = NSUserDefaults.standardUserDefaults
        return userDefaults.stringForKey(loginKey)
    }

    actual fun clearLogin() {
        val userDefaults = NSUserDefaults.standardUserDefaults
        userDefaults.removeObjectForKey(loginKey)
    }
}