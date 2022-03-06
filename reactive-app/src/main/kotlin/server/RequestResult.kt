package server

import io.netty.handler.codec.http.HttpResponseStatus
import rx.Observable

data class RequestResult(val status: HttpResponseStatus, val message: Observable<String>)