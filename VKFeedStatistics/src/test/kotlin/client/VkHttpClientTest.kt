package client

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import protocol.VkApiConfig
import protocol.VkApiResponse
import protocol.VkQueryBuilder
import java.time.Duration
import java.time.Instant


class VkHttpClientTest {

    private var vkConfig = VkApiConfig(
        """{
            "port": 443,
            "version" : {
                "major": 5,
                "minor": 131
            }
        }""".byteInputStream(),
        """{"access_token":"785kfghdfghdg987e457y34gdgud"}""".byteInputStream()
    )

    @Mock
    private lateinit var httpClient: TrivialHttpClient

    private lateinit var vkClient: VkHttpClient

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        vkClient = VkHttpClient(vkConfig, httpClient)
    }

    private suspend fun countTest(hashTag: String, resp: String, cnt: Int, stSec: Long, endSec: Long) {
        Mockito.`when`(
            httpClient.get(
                VkQueryBuilder(vkConfig).buildHashTagCntQuery(
                    hashTag,
                    stSec,
                    endSec
                )
            )
        ).thenReturn(resp)
        assertEquals(
            vkClient.countPostByHashtag(hashTag, stSec, endSec),
            VkApiResponse(VkApiResponse.InnerResponse(cnt))
        )
    }

    @Test
    fun countHashTagSingleRequest(): Unit = runBlocking {
        val end = Instant.now()
        val st = (end - Duration.ofHours(1))
        countTest(
            "news",
            """{"response": {"total_count": 10}}""",
            10,
            st.epochSecond,
            end.epochSecond
        )
    }

    @Test
    fun countHashTagMultipleRequest(): Unit = runBlocking {
        val now = Instant.now()
        for (i in 1L..24L) {
            countTest(
                "tag${i}",
                """{"response": {"total_count": ${i}}}""",
                i.toInt(),
                (now - Duration.ofHours(i)).epochSecond,
                (now - Duration.ofHours(0)).epochSecond
            )
        }
    }

    @Test
    fun countHashTagComplexResponse(): Unit = runBlocking {
        val now = Instant.now()
        countTest(
            "ITMO",
            """{"response":{"items":[],"count":366,"total_count":366}}""",
            366,
            (now - Duration.ofHours(1)).epochSecond,
            (now - Duration.ofHours(0)).epochSecond
        )
    }
}