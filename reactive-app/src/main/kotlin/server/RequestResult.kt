package server

import io.netty.handler.codec.http.HttpResponseStatus
import rx.Observable

data class RequestResult(val status: HttpResponseStatus, val message: Observable<String>) {
    companion object {
        fun failWith(msg: String): RequestResult {
            return RequestResult(HttpResponseStatus.BAD_REQUEST, Observable.just(msg))
        }
    }
}