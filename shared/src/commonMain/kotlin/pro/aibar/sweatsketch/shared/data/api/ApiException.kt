package pro.aibar.sweatsketch.shared.data.api

import io.ktor.http.HttpStatusCode

class ApiException(val status: HttpStatusCode, message: String) : Exception(message)