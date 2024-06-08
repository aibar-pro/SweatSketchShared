package pro.aibar.sweatsketch.shared.di

import org.koin.core.Koin
import org.koin.core.context.startKoin

fun initKoin(baseUrl: String): Koin {
    val koinApplication = startKoin {
        modules(createKoinModule(baseUrl))
    }
    return koinApplication.koin
}