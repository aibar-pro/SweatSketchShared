package pro.aibar.sweatsketch.shared.util

import platform.Foundation.NSUUID
import platform.Foundation.NSUserDefaults

actual object KeyStorage {
    private const val DEVICE_ID_KEY = "deviceId"
    private const val USER_ID_KEY = "userId"

    actual suspend fun getDeviceId(): String {
        val userDefaults = NSUserDefaults.standardUserDefaults
        var deviceId = userDefaults.stringForKey(DEVICE_ID_KEY)
        if (deviceId == null) {
            deviceId = NSUUID.UUID().toString()
            userDefaults.setObject(deviceId, forKey = DEVICE_ID_KEY)
        }
        return deviceId
    }

    actual suspend fun saveUserId(userId: String) {
        val userDefaults = NSUserDefaults.standardUserDefaults
        userDefaults.setObject(userId, forKey = USER_ID_KEY)
    }

    actual suspend fun getUserId(): String? {
        val userDefaults = NSUserDefaults.standardUserDefaults
        return userDefaults.stringForKey(USER_ID_KEY)
    }

    actual suspend fun clearUserId() {
        val userDefaults = NSUserDefaults.standardUserDefaults
        userDefaults.removeObjectForKey(USER_ID_KEY)
    }
}