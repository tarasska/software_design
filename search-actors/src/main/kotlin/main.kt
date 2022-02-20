import actors.MasterActor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.PatternsCS.ask
import search.client.SearchRequest
import search.server.StubServer
import java.time.Duration

fun withServer(action: () -> Unit) {
    val server = StubServer()
    try {
        server.start()
        action()
    } finally {
        server.stop()
    }
}


fun main(args: Array<String>) {
    withServer {
        val system = ActorSystem.create("search")

        println("Enter query:")
        val query = "request"
        val master = system.actorOf(Props.create(MasterActor::class.java))
        val answer = ask(master, SearchRequest(query, 5), Duration.ofSeconds(30)).toCompletableFuture().join()

        if (answer is List<*>) {
            for (res in answer) {
                println(res.toString())
            }
        }
    }
}
