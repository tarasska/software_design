package search

import search.client.SearchRequest
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object SearchUtils {
    val recordCntName = "c"

    fun createDefaultUri(engine: SearchEngine, request: SearchRequest): URI =
        URI.create(
            "http://localhost:8080" +
                    "/${engine.name}" +
                    "/${URLEncoder.encode(request.query, StandardCharsets.UTF_8)}" +
                    "?${recordCntName}=${request.recordsCnt}"
        )

    fun parseDefaultUriPath(path: String): Pair<String, String>? {
        val parts = path.split("/")
        if (parts.size < 3) {
            return null
        }
        return parts[1] to URLDecoder.decode(parts[2], StandardCharsets.UTF_8)
    }
}