package client

import protocol.VkApiResponse
import java.io.Closeable

interface VkClient : Closeable {
    suspend fun countPostByHashtag(hashTag: String, hoursLimit: Long) : VkApiResponse?
}