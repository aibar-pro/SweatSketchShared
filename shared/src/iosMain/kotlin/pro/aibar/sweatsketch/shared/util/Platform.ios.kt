package pro.aibar.sweatsketch.shared.util

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual object Platform {
    actual fun currentTimeMillis(): ULong {
        return (NSDate().timeIntervalSince1970 * 1000).toULong()
    }
}