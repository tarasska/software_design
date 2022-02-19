package search.client

import com.beust.klaxon.Klaxon
import search.*
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class StubSearchClient(private val engine: SearchEngine) : SearchClient {

    private val client: HttpClient = HttpClient.newHttpClient()

    private fun buildHttpRequest(request: SearchRequest): HttpRequest {
        return HttpRequest.newBuilder()
            .GET()
            .uri(SearchUtils.createDefaultUri(engine, request))
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