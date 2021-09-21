package client

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.get
import protocol.VkApiResponse

class VkHttpClient : VkClient {
    private val httpClient : HttpClient = HttpClient(Apache)

    override suspend fun countPostByHashtag(hashtag: String, hoursLimit: Int): VkApiResponse? {
        val query =  "Query here"
        val rawResponse: String = httpClient.get(query)
        return Klaxon().parse<VkApiResponse>(rawResponse)
    }

    override fun close() = httpClient.close()
}
