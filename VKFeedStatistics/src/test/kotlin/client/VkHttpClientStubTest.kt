package client

import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.status
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.method
import com.xebialabs.restito.semantics.Condition.startsWithUri
import com.xebialabs.restito.server.StubServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import protocol.ErrorResponse
import protocol.HashTagCntResponse
import protocol.VkApiConfig

import java.util.function.Consumer




class VkHttpClientStubTest {
    companion object {
        private const val PORT: Int = 32453
        private lateinit var vkClient: VkHttpClient

        @JvmStatic
        @BeforeAll
        fun setup() {
            val vkConfig = VkApiConfig("""{
                "protocol": "http",
                "host": "localhost",
                "port": 32453,
                "version" : {
                    "major": 5,
                    "minor": 131
                }
            }""".byteInputStream(),
                """{"access_token":"785kfghdfghdg987e457y34gdgud"}""".byteInputStream()
            )
            vkClient = VkHttpClient(vkConfig)
        }
    }

    private suspend fun withStubServer(port: Int, callback: suspend (StubServer) -> Unit) {
        var stubServer: StubServer? = null
        try {
            stubServer = StubServer(port).run()
            callback(stubServer)
        } finally {
            stubServer?.stop()
        }
    }

    @Test
    fun okStubResponseTest() = runBlocking {
        withStubServer(PORT) {
            whenHttp(it)
                .match(method(Method.GET), startsWithUri("/method/newsfeed.search"))
                .then(
                    stringContent("""{"response":{"items":[],"count":366,"total_count":366}}""")
                )
            val res = vkClient.countPostByHashtag("hashtag", 0, 0)
            assertInstanceOf(HashTagCntResponse::class.java, res)
            assertEquals((res as HashTagCntResponse).response.postCount, 366)
        }
    }

    @Test
    fun errorResponseTest() = runBlocking {
        withStubServer(PORT) {
            whenHttp(it)
                .match(method(Method.GET), startsWithUri("/method/newsfeed.search"))
                .then(
                    stringContent("""{
                        "error": {
                            "error_code": 29,
                            "error_msg": "Rate limit reached"
                        }
                    }""")
                )
            val res = vkClient.countPostByHashtag("hashtag", 0, 0)
            assertInstanceOf(ErrorResponse::class.java, res)
            assertEquals(
                (res as ErrorResponse).error,
                ErrorResponse.ErrorInfo(29, "Rate limit reached")
            )
        }
    }
}
