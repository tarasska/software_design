import akka.pattern.Patterns.ask
import akka.testkit.javadsl.TestKit
import org.junit.jupiter.api.Assertions.assertTrue
import search.SearchEngine
import search.SearchResult
import search.client.SearchClient
import search.client.SearchRequest
import search.client.StubSearchClient
import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.*

class ActorsTest: ActorsBaseTest() {

    @Test
    fun сhildTest() {
        val engine = SearchEngine.YAHOO
        val child = createChild(engine)

        val res: SearchResult
            = ask(child, SearchRequest("test_query", 3), Duration.ofSeconds(5))
            .toCompletableFuture()
            .join() as SearchResult

        assertEquals(res.engine, engine)
        assertEquals(res.resultElements.size, 3)
        assertTrue(res.resultElements.all { elem -> elem.title.contains("test_query") })
    }

    @Test
    fun masterOkTest() {
        object : TestKit(system) {
            init {
                val master = createMaster(SearchEngine.values().map {
                        engine -> StubSearchClient(engine)
                })
                within(Duration.ofSeconds(3)) {
                    master.tell(SearchRequest("Blabla", 10), testActor)
                    assertTrue(expectMsgAnyClassOf<List<SearchResult>>(List::class.java).all {
                        it.resultElements.size == 10
                    })
                }
            }
        }
    }


    class FakeClient : SearchClient {
        override fun requestTopRecords(request: SearchRequest): SearchResult = runBlocking {
            delay(Duration.ofSeconds(15).toMillis())
            throw RuntimeException("Fake client")
        }
    }

    @Test
    fun masterFailTest() {
        object : TestKit(system) {
            init {
                val allClients = SearchEngine.values().map {
                        engine -> StubSearchClient(engine)
                }
                val master = createMaster(allClients.plus(FakeClient()))
                master.tell(SearchRequest("kuku", 5), testActor)

                expectNoMessage(Duration.ofSeconds(5))
                within(Duration.ofSeconds(3)) {
                    assertEquals(
                        SearchEngine.values().size,
                        expectMsgAnyClassOf<List<SearchResult>>(List::class.java).size
                    )
                }
            }
        }
    }

}