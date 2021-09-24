package protocol

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class VkApiResponseTest {
    @Test
    fun onlyTotalCntTest() {
        assertDoesNotThrow {
            val res = VkApiResponse.of("""{"response": {"total_count": 10}}""")
            assertTrue(res is HashTagCntResponse)
            assertEquals(10, (res as HashTagCntResponse).response.postCount)
        }
    }

    @Test
    fun commonResponseTest() {
        assertDoesNotThrow {
            val res = VkApiResponse.of("""{"response":{"items":[],"count":123,"total_count":123}}""")
            assertInstanceOf(HashTagCntResponse::class.java, res)
            assertEquals(123, (res as HashTagCntResponse).response.postCount)
        }
    }

    @Test
    fun nonStdKeysTest() {
        assertDoesNotThrow {
            val res = VkApiResponse.of(
                """{
                        "error": {
                            "error_code": 29,
                            "error_msg": "Rate limit reached"
                        }
                    }""")
            assertInstanceOf(ErrorResponse::class.java, res)
        }
    }

    @Test
    fun brokenResponse() {
        assertDoesNotThrow {
            val res = VkApiResponse.of(
                """{
                        "error": {
                            "error_code": 29,
                            "error_msg": "Rate limit reached",
                            "bcbfbdfb
                        }
                    }""")
            assertInstanceOf(UndefinedResponse::class.java, res)
        }
    }
}