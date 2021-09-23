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

class VkHttpClient(
    config: VkApiConfig,
    private val httpClient: HttpClient = HttpClient(Apache)
): VkClient {
    
    private val queryBuilder = VkQueryBuilder(config)

    override suspend fun countPostByHashtag(hashTag: String, hoursLimit: Long): VkApiResponse? {
        val requestTime = Instant.now()
        return countPostByHashtag(
            hashTag,
            (requestTime - Duration.ofHours(hoursLimit)).epochSecond,
            requestTime.epochSecond
        )
    }

    override suspend fun countPostByHashtag(hashTag: String, startTimeSec: Long, endTimeSec: Long): VkApiResponse? {
        val rawResponse: String = httpClient.get(
            queryBuilder.buildHashTagCntQuery(
                hashTag,
                startTimeSec,
                endTimeSec
            )
        )
        return Klaxon().parse<VkApiResponse>(rawResponse)
    }

    override fun close() = httpClient.close()
}
