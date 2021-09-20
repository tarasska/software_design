package client

import io.ktor.client.*

class VkHttpClient : VkClient {
    private val httpClient : HttpClient = HttpClient()

    override suspend fun countPostByHashtag(hashtag: String, hoursLimit: Int) {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}