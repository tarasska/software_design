package client

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.get
import protocol.VkApiConfig
import protocol.VkApiResponse
import protocol.VkQueryBuilder
import java.time.Duration
import java.time.Instant

class VkHttpClient(config: VkApiConfig) : VkClient {
    private val httpClient: HttpClient = HttpClient(Apache)
    private val queryBuilder = VkQueryBuilder(config)

    override suspend fun countPostByHashtag(hashTag: String, hoursLimit: Long): VkApiResponse? {
        val requestTime = Instant.now()
        val rawResponse: String = httpClient.get(
            queryBuilder.buildHashTagCntQuery(
                hashTag,
                (requestTime - Duration.ofHours(hoursLimit)).epochSecond,
                requestTime.epochSecond
            )
        )
        return Klaxon().parse<VkApiResponse>(rawResponse)
    }

    override fun close() = httpClient.close()
}
