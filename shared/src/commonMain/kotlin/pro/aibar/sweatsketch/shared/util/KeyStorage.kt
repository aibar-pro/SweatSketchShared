package pro.aibar.sweatsketch.shared.util

expect object KeyStorage {
    fun getDeviceId(): String
    fun saveLogin(login: String)
    fun getLogin(): String?
    fun clearLogin()
}