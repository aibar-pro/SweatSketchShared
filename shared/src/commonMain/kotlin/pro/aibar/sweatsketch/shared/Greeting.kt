package pro.aibar.sweatsketch.shared

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class Greeting {
    private val client = HttpClient()

    suspend fun greeting(): String {
        val response = client.get("http://0.0.0.0:8080/health-check") {
            contentType(ContentType.Application.Json)
//            bearerAuth()
        }
        return response.bodyAsText()
    }
}