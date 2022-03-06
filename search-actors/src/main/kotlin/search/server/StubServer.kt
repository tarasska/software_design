package search.server

import com.beust.klaxon.Klaxon
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import search.SearchEngine
import search.SearchResultElement
import search.SearchUtils
import java.lang.Thread.sleep


class StubServer: SearchHttpServer {
    private val fail = Response(Status.BAD_REQUEST)
    private val defaultRecordsCnt = 5

    private var badEngine: SearchEngine? = null

    override fun handler(): HttpHandler = { request: Request -> createResponse(request) }

    public fun setupBadEngine(engine: SearchEngine){
        this.badEngine = engine
    }

    private fun createResponse(request: Request): Response {
        val (engine, query) = SearchUtils.parseDefaultUriPath(request.uri.path) ?: return fail

        if (badEngine !== null && engine == badEngine!!.name) {
            sleep(10000L)
            return fail
        }
        val recordsCnt = parseRecordsCount(request)

        return Response(Status.OK).body(Klaxon().toJsonString(generateRecords(engine, query, recordsCnt)))
    }

    private fun parseRecordsCount(request: Request): Int =
        request.query(SearchUtils.recordCntName)?.toIntOrNull() ?: defaultRecordsCnt

    private fun generateRecords(engine: String, query: String, recordsCnt: Int): List<SearchResultElement> {
        val records = mutableListOf<SearchResultElement>()
        for (i in 1..recordsCnt) {
            records.add(SearchResultElement(
                engine,
                "Link $i [query: ${query}]"
            ))
        }
        return records
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            StubServer().start()
        }
    }
}