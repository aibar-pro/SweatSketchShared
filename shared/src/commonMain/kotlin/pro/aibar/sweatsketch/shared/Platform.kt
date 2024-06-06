package pro.aibar.sweatsketch.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform