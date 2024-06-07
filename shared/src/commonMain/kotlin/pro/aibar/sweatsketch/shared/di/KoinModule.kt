package pro.aibar.sweatsketch.shared.di

import io.ktor.client.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pro.aibar.sweatsketch.shared.data.api.AuthApi
import pro.aibar.sweatsketch.shared.data.api.AuthApiImpl
import pro.aibar.sweatsketch.shared.data.repository.AuthRepository
import pro.aibar.sweatsketch.shared.data.repository.AuthRepositoryImpl
import pro.aibar.sweatsketch.shared.util.SecureStorage
import pro.aibar.sweatsketch.shared.util.TokenManager

//fun initKoin(appContext: Context) {
//    startKoin {
//        modules(
//            module {
//                single { HttpClient {
//                    install(JsonFeature) {
//                        serializer = KotlinxSerializer()
//                    }
//                    install(Logging) {
//                        level = LogLevel.BODY
//                    }
//                } }
//                single { AuthApiImpl(get(), "https://your.api.base.url") as AuthApi }
//                single { AuthRepositoryImpl(get()) as AuthRepository }
//                single { UserRepositoryImpl(get(), get()) as UserRepository }
//                single { WorkoutRepositoryImpl(get()) as WorkoutRepository }
//                single { SecureStorage.initialize(appContext) }
//                single { TokenManager }
//            }
//        )
//    }
//}