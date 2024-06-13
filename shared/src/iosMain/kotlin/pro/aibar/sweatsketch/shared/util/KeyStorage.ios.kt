package pro.aibar.sweatsketch.shared.util

import platform.Foundation.NSUUID
import platform.Foundation.NSUserDefaults

actual object KeyStorage {
    private const val deviceIdKey = "deviceId"
    actual fun getDeviceId(): String {
        val userDefaults = NSUserDefaults.standardUserDefaults
        var deviceId = userDefaults.stringForKey(deviceIdKey)
        if (deviceId == null) {
            deviceId = NSUUID.UUID().toString()
            userDefaults.setObject(deviceId, forKey = deviceIdKey)
        }
        return deviceId
    }
}