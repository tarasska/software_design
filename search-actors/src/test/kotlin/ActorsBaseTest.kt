import actors.ChildActor
import actors.MasterActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import search.SearchEngine
import search.client.SearchClient
import search.client.StubSearchClient
import search.server.StubServer

open class ActorsBaseTest {
    protected var system: ActorSystem? = null

    companion object {
        protected val server = StubServer()

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            server.start()
        }
    }

    @BeforeEach
    fun setupSystem() {
        system = ActorSystem.create("ActorsTest")
    }

    @AfterEach
    fun clean() {
        system!!.terminate()
        system = null
    }

    fun createChild(engine: SearchEngine): ActorRef {
        return system!!.actorOf(Props.create(ChildActor::class.java, StubSearchClient(engine)))
    }

    fun createMaster(clients: List<SearchClient>): ActorRef {
        return system!!.actorOf(Props.create(MasterActor::class.java, clients))
    }
}