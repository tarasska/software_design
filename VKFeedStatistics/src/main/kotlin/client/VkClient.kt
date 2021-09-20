package client

import java.io.Closeable

interface VkClient : Closeable {
    suspend fun countPostByHashtag(hashtag: String, hoursLimit: Int)
}