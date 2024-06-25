package pro.aibar.sweatsketch.shared.util

import io.ktor.utils.io.core.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDataGetBytes
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFRangeMake
import platform.CoreFoundation.CFStringCreateWithCString
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanFalse
import platform.CoreFoundation.kCFBooleanTrue
import platform.CoreFoundation.kCFStringEncodingUTF8
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.SecItemUpdate
import platform.Security.errSecItemNotFound
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccount
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

@OptIn(ExperimentalForeignApi::class)
actual object SecureStorage {
    private const val refreshTokenKey = "refreshToken"

    actual fun saveRefreshToken(token: String) {
        val tokenData = token.toByteArray().toNSData()

        val query = CFDictionaryCreateMutable(null, 2, null, null)
        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, refreshTokenKey.toCFType())

        val attributesToUpdate = CFDictionaryCreateMutable(null, 1, null, null)
        CFDictionaryAddValue(attributesToUpdate, kSecValueData, tokenData.toCFType() )

        // Try to update item
        var status = SecItemUpdate(query, attributesToUpdate)

        // If the item doesn't exist, add it
        if (status == errSecItemNotFound) {
            val newItem = CFDictionaryCreateMutable(null, 3, null, null)
            CFDictionaryAddValue(newItem, kSecClass, kSecClassGenericPassword)
            CFDictionaryAddValue(newItem, kSecAttrAccount, refreshTokenKey.toCFType())
            CFDictionaryAddValue(newItem, kSecValueData, tokenData.toCFType()  )

            status = SecItemAdd(newItem, null)
        }

        if (status != errSecSuccess) {
            throw Exception("Error saving refresh token to KeyChain: $status")
        }

        println("KMM: Refresh token SAVE")
    }

    actual fun getRefreshToken(): String? {
        val query = CFDictionaryCreateMutable(null, 4, null, null)
        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, refreshTokenKey.toCFType())
        CFDictionaryAddValue(query, kSecReturnData, kCFBooleanTrue)
        CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)

        val dataRef = nativeHeap.alloc<CFTypeRefVar>()
        val status = SecItemCopyMatching(query, dataRef.ptr)

        return if (status == errSecSuccess) {
            val cfData = dataRef.value?.let { it as? CFDataRef }
            if (cfData != null) {
                val length = CFDataGetLength(cfData).toLong()
                val bytes = ByteArray(length.toInt())
                bytes.usePinned { pinnedBytes ->
                    CFDataGetBytes(cfData, CFRangeMake(0, length), pinnedBytes.addressOf(0).reinterpret())
                }
                println("KMM: Refresh token GET")
                bytes.decodeToString()
            } else {
                throw Exception("Error: Retrieved data is not CFDataRef")
            }
        } else {
            throw Exception("Error while searching for a refresh token, status: `$status`")
        }
    }

    actual fun clearRefreshToken() {
        val query = CFDictionaryCreateMutable(null, 2, null, null)
        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, refreshTokenKey.toCFType())

        val status = SecItemDelete(query)

        if (status != errSecSuccess && status != errSecItemNotFound) {
            throw Exception("Error clearing refresh token from KeyChain: $status")
        }

        println("KMM: Refresh token CLEAR")
    }
}

//@OptIn(ExperimentalForeignApi::class)
//fun NSData.toByteArray(): ByteArray {
//    return ByteArray(length.toInt()).apply {
//        usePinned {
//            memcpy(it.addressOf(0), bytes, length)
//        }
//    }
//}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}

//@OptIn(ExperimentalForeignApi::class)
//fun createDictionary(vararg pairs: Pair<CFStringRef, Any?>): CFMutableDictionaryRef = memScoped {
//    val dict = CFDictionaryCreateMutable(null, pairs.size.toLong(), null, null)
//    pairs.forEach { (key, value) ->
//        CFDictionaryAddValue(dict, key, value.toCFType())
//    }
//    dict!!
//}

@OptIn(ExperimentalForeignApi::class)
fun Any?.toCFType(): CFTypeRef? = when (this) {
    is String -> CFStringCreateWithCString(null, this, kCFStringEncodingUTF8)
    is NSData -> CFBridgingRetain(this)
    is Boolean -> if (this) kCFBooleanTrue else kCFBooleanFalse
//    is Int -> CFNumberCreate(null, kCFNumberIntType, this)
    else -> null
}
