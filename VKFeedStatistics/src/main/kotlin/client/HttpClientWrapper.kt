package client

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.get

class HttpClientWrapper : TrivialHttpClient {
    private val httpClient = HttpClient(Apache)

    override suspend fun get(query: String): String {
        return httpClient.get(query)
    }

    override fun close() {
        httpClient.close()
    }
}