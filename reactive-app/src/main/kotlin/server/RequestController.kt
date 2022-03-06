package server

import io.netty.handler.codec.http.HttpResponseStatus
import rx.Observable

object RequestController {
    fun handle(endpoint: String, params: Map<String, List<String>>): RequestResult {
        return RequestResult(HttpResponseStatus.ACCEPTED, Observable.just(""))
    }
}