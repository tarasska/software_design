package actors

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import akka.japi.pf.ReceiveBuilder
import search.SearchEngine
import search.SearchResult
import search.client.SearchClient
import search.client.SearchRequest
import search.client.StubSearchClient
import java.time.Duration

class MasterActor(
    private val clients: List<SearchClient> = SearchEngine.values().map {
            engine -> StubSearchClient(engine)
    }
): AbstractActor() {
    private class TimeoutMsg

    private lateinit var callBack: ActorRef
    private val results = mutableListOf<SearchResult>()

    private fun setupTimeout() = context.system.scheduler.scheduleOnce(
        Duration.ofSeconds(5),
        self,
        TimeoutMsg(),
        context.system.dispatcher,
        ActorRef.noSender()
    )

    private fun finishOrWait() {
        if (results.size != clients.size) {
            return
        }
        callBack.tell(results, self)
        context.stop(self)
    }

    private fun mapToChild(request: SearchRequest) {
        println(request)
        callBack = sender
        clients.forEach { client ->
            context.actorOf(Props.create(ChildActor::class.java, client)).tell(request, self)
        }
        setupTimeout()
    }

    private fun aggregateResult(result: SearchResult) {
        results.add(result)
        finishOrWait()
    }

    private fun returnOnTimeout() {
        callBack.tell(results, self)
    }

    override fun createReceive(): Receive {
        return ReceiveBuilder()
            .match(SearchRequest::class.java, this::mapToChild)
            .match(TimeoutMsg::class.java) { returnOnTimeout() }
            .match(SearchResult::class.java, this::aggregateResult)
            .build()
    }
}