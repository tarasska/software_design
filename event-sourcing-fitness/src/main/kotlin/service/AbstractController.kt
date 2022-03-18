package service

import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import model.event.DbConstants
import model.event.DbFormatter
import rx.Observable
import java.time.LocalDateTime

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

    protected fun extractLongParam(name: String, params: Map<String, List<String>>): Long {
        return extractParam(name, params).toLong()
    }

    protected fun extractTimeParam(name: String, params: Map<String, List<String>>): LocalDateTime {
        return LocalDateTime.parse(extractParam(name, params), DbFormatter.short)
    }

    protected fun extractParam(name: String, params: Map<String, List<String>>): String {
        return (params[name] ?: error("Required parameter $name not found "))[0]
    }

    protected fun <T> mapToStr(observable: Observable<T>): Observable<String> {
        return observable.map { o -> o.toString() }.onErrorReturn { t -> t.message }
    }
}