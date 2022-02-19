package actors

import akka.actor.AbstractActor
import akka.japi.pf.ReceiveBuilder
import search.client.SearchClient
import search.client.SearchRequest

class ChildActor(private val client: SearchClient) : AbstractActor() {

    private fun sendRequest(request: SearchRequest) {
        val res = client.requestTopRecords(request)
        sender.tell(res, self)
    }

    override fun createReceive(): Receive {
        return ReceiveBuilder()
            .match(SearchRequest::class.java, this::sendRequest)
            .build()
    }
}