package pro.aibar.sweatsketch.shared.util

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

actual object KeyStorage {
    private const val PREFS_NAME = "shared_prefs"
    private const val DEVICE_ID_KEY = "device_id"
    private const val LOGIN_KEY = "login"

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    actual fun getDeviceId(): String {
        var deviceId = sharedPreferences.getString(DEVICE_ID_KEY, null)
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(DEVICE_ID_KEY, deviceId).apply()
        }
        return deviceId
    }

    actual fun saveLogin(login: String) {
        sharedPreferences.edit().putString(LOGIN_KEY, login).apply()
    }

    actual fun getLogin(): String? {
        return sharedPreferences.getString(LOGIN_KEY, null)
    }

    actual fun clearLogin() {
        sharedPreferences.edit().remove(LOGIN_KEY).apply()
    }
}