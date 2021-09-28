package client

import java.io.Closeable

interface TrivialHttpClient: Closeable {
    suspend fun get(query: String): String
}