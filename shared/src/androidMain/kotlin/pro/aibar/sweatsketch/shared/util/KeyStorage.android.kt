package pro.aibar.sweatsketch.shared.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.util.UUID

private const val PREFS_NAME = "shared_prefs"
private val Context.dataStore by preferencesDataStore(name = PREFS_NAME)

actual object KeyStorage {
    private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
    private val USER_ID_KEY = stringPreferencesKey("login")

    @Volatile
    private var _ds: androidx.datastore.core.DataStore<Preferences>? = null
    private val ds
        get() = _ds ?: error("KeyStorage not initialised!")

    fun initialize(context: Context) {
        if (_ds == null) {
            _ds = context.applicationContext.dataStore
        }
    }

    actual suspend fun getDeviceId(): String  {
        var deviceId = ds.data.first()[DEVICE_ID_KEY]
        if (deviceId != null) return deviceId

        val newId = UUID.randomUUID().toString()
        ds.edit { it[DEVICE_ID_KEY] = newId }
        return newId
    }

    actual suspend fun saveUserId(userId: String) {
        ds.edit { it[USER_ID_KEY] = userId }
    }

    actual suspend fun getUserId(): String? {
        return ds.data.first()[USER_ID_KEY]
    }

    actual suspend fun clearUserId() {
        ds.edit { it.remove(USER_ID_KEY) }
    }
}