package protocol

import com.beust.klaxon.Klaxon
import java.io.InputStream

class VkApiConfig(publicConfig: InputStream, privateConfig: InputStream) {
    data class Version(val major: Int, val minor: Int)
    data class PublicConfig(
        val protocol: String = "https",
        val host: String = "api.vk.com",
        val port: Int = 443,
        val version: Version
    )
    data class PrivateConfig(val access_token: String)

    val publicConfig: PublicConfig = Klaxon().parse(
        publicConfig.bufferedReader().lines().toArray().joinToString()
    )!!

    val privateConfig: PrivateConfig = Klaxon().parse(
        privateConfig.bufferedReader().lines().toArray().joinToString()
    )!!
}