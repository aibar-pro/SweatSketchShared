package pro.aibar.sweatsketch.shared.util

import android.content.Context
import android.content.SharedPreferences

actual object SecureStorage {
    private const val PREFS_NAME = "secure_prefs"
    private const val REFRESH_TOKEN_KEY = "refresh_token"

    private lateinit var sharedPreferences: SharedPreferences

    /*
    // To ensure SharedPreferences is initialized properly, call the initialize method with the application context when the app starts.
    class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SecureStorage.initialize(this)
    }
     */
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    // TODO: Implement Android SECURE storage
    actual fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, token).apply()
    }

    actual fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    actual fun clearRefreshToken() {
        sharedPreferences.edit().remove(REFRESH_TOKEN_KEY).apply()
    }
}