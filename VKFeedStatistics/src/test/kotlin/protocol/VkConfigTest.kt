package protocol

import com.beust.klaxon.KlaxonException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class VkConfigTest {
    @Test
    fun testParsingWithDefaults() {
        val config = VkApiConfig(
            ("""{
                "port": 443,
                "version": {
                    "major": 5,
                    "minor": 135
                }
            }""").byteInputStream(),
            "{\"access_token\": \"abc001efg002zzz\"}".byteInputStream()
        )
        assertEquals(443, config.publicConfig.port)
        assertEquals(5, config.publicConfig.version.major)
        assertEquals(135, config.publicConfig.version.minor)
        assertEquals("abc001efg002zzz", config.privateConfig.access_token)
    }

    @Test
    fun testFullParsing() {
        val config = VkApiConfig(
            ("""{
                "protocol": "http"
                "host": "localhost"
                "port": 443,
                "version": {
                    "major": 5,
                    "minor": 135
                }
            }""").byteInputStream(),
            "{\"access_token\": \"abc001efg002zzz\"}".byteInputStream()
        )
        assertEquals("http", config.publicConfig.protocol)
        assertEquals("localhost", config.publicConfig.host)
        assertEquals(443, config.publicConfig.port)
        assertEquals(5, config.publicConfig.version.major)
        assertEquals(135, config.publicConfig.version.minor)
        assertEquals("abc001efg002zzz", config.privateConfig.access_token)
    }

    @Test
    fun testUnsuccessfulParsing() {
        assertThrows(KlaxonException::class.java) {
            VkApiConfig(
                "{\"port\": 443, \"version\": 366}".byteInputStream(),
                "{access_token=007}".byteInputStream()
            )
        }
    }
}