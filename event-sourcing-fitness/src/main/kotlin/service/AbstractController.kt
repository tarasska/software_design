package service

import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import rx.Observable

abstract class AbstractController {
    abstract fun handle(endpoint: String, params: Map<String, List<String>>): Observable<String>

    fun handle(
        request: HttpServerRequest<ByteBuf>,
        response: HttpServerResponse<ByteBuf>
    ): Observable<Void> {
        val requestUri = request.decodedPath
        val result: Observable<String> = if (requestUri.isNullOrEmpty()) {
            Observable.just("Invalid request.")
        } else {
            handle(requestUri.substring(1), request.queryParameters)
        }
        return response.writeString(result)
    }

    protected fun extractIntParam(name: String, params: Map<String, List<String>>): Int {
        return extractParam(name, params).toInt()
    }

    protected fun extractParam(name: String, params: Map<String, List<String>>): String {
        return (params[name] ?: error("Required parameter $name not found "))[0]
    }
}