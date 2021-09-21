package client

import protocol.VkApiResponse
import java.io.Closeable

interface VkClient : Closeable {
    suspend fun countPostByHashtag(hashtag: String, hoursLimit: Long) : VkApiResponse?
}