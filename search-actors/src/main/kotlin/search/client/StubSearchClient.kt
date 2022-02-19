package search.client

import com.beust.klaxon.Klaxon
import search.SearchEngine
import search.SearchResult
import search.SearchResultElement
import search.buildFailedResult
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

class StubSearchClient(private val engine: SearchEngine) : SearchClient {

    private val client: HttpClient = HttpClient.newHttpClient()

    private fun buildHttpRequest(request: SearchRequest): HttpRequest {
        return HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(
                "http://localhost:8080" +
                    "/${engine.name}" +
                    "/${URLEncoder.encode(request.query, StandardCharsets.UTF_8)}"+
                    "?c=${request.recordsCnt}"))
            .build()
    }

    override fun requestTopRecords(request: SearchRequest): SearchResult {
        val response = client.send(buildHttpRequest(request), HttpResponse.BodyHandlers.ofString())
        val responseContent = Klaxon().parseArray<SearchResultElement>(response.body())
        return if (responseContent !== null) {
            SearchResult(engine, responseContent)
        } else {
            buildFailedResult(engine)
        }
    }
}