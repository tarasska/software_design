import app.HashTagCounter
import client.VkHttpClient
import kotlinx.coroutines.runBlocking
import protocol.VkApiConfig
import java.io.File
import java.io.FileInputStream

fun main(args: Array<String>) {
    val config = VkApiConfig(
        FileInputStream(File("src/main/resources/config.public.json")),
        FileInputStream(File("src/main/resources/config.private.json"))
    )
    VkHttpClient(config).use {
        val res = runBlocking { HashTagCounter(it).count("news", 5) }
        print(res)
    }
}