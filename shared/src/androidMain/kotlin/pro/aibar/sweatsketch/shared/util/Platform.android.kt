package pro.aibar.sweatsketch.shared.util

actual object Platform {
    actual fun currentTimeMillis(): ULong {
        return System.currentTimeMillis().toULong()
    }
}