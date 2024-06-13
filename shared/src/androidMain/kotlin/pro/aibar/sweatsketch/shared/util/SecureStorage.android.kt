package pro.aibar.sweatsketch.shared.util

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

actual object SecureStorage {
    private const val KEY_ALIAS = "refresh_token_key"
    private const val PREFS_NAME = "secure_prefs"
    private const val REFRESH_TOKEN_KEY = "refresh_token"
    private const val IV_KEY = "iv"

    private lateinit var encryptedSharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        createKey()
    }

    private fun createKey() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    private fun getCipher(mode: Int, iv: ByteArray? = null): Cipher {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(mode, secretKey)
        } else {
            cipher.init(mode, secretKey, GCMParameterSpec(128, iv))
        }
        return cipher
    }

    actual fun saveRefreshToken(token: String) {
        val cipher = getCipher(Cipher.ENCRYPT_MODE)
        val iv = cipher.iv
        val encryptedToken = cipher.doFinal(token.toByteArray(Charsets.UTF_8))

        val encryptedTokenString = Base64.encodeToString(encryptedToken, Base64.DEFAULT)
        val ivString = Base64.encodeToString(iv, Base64.DEFAULT)

        encryptedSharedPreferences.edit().putString(REFRESH_TOKEN_KEY, encryptedTokenString)
            .putString(IV_KEY, ivString)
            .apply()
    }

    actual fun getRefreshToken(): String? {
        val encryptedTokenString = encryptedSharedPreferences.getString(REFRESH_TOKEN_KEY, null) ?: return null
        val ivString = encryptedSharedPreferences.getString(IV_KEY, null) ?: return null

        val encryptedToken = Base64.decode(encryptedTokenString, Base64.DEFAULT)
        val iv = Base64.decode(ivString, Base64.DEFAULT)
        val cipher = getCipher(Cipher.DECRYPT_MODE, iv)
        val decryptedToken = cipher.doFinal(encryptedToken)
        return String(decryptedToken, Charsets.UTF_8)
    }

    actual fun clearRefreshToken() {
        encryptedSharedPreferences.edit().remove(REFRESH_TOKEN_KEY).remove(IV_KEY).apply()
    }
}