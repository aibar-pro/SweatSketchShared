package pro.aibar.sweatsketch.shared.util

import android.content.Context

object StorageInitializer {
    /*
    class SweatSketchApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            StorageInitializer.initialize(this)
        }
    }
     */
    fun initialize(context: Context) {
        SecureStorage.initialize(context)
        KeyStorage.initialize(context)
    }
}