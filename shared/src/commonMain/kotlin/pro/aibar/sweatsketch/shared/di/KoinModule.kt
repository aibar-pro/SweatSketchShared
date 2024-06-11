package pro.aibar.sweatsketch.shared.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.Module
import org.koin.dsl.module
import pro.aibar.sweatsketch.shared.data.api.AuthApi
import pro.aibar.sweatsketch.shared.data.api.AuthApiImpl
import pro.aibar.sweatsketch.shared.data.api.UserApi
import pro.aibar.sweatsketch.shared.data.api.UserApiImpl
import pro.aibar.sweatsketch.shared.data.repository.AuthRepository
import pro.aibar.sweatsketch.shared.data.repository.AuthRepositoryImpl
import pro.aibar.sweatsketch.shared.data.repository.UserRepository
import pro.aibar.sweatsketch.shared.data.repository.UserRepositoryImpl
import pro.aibar.sweatsketch.shared.util.TokenManager

fun createKoinModule(baseUrl: String): Module {
    return module {
        single { HttpClient {
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                level = LogLevel.BODY
            }
        } }
        single { TokenManager }
        single<AuthApi> { AuthApiImpl(get(), baseUrl, get()) }
        single<AuthRepository> { AuthRepositoryImpl(get()) }
        single<UserApi> { UserApiImpl(get(), baseUrl, get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }
}
