import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import org.apache.log4j.BasicConfigurator
import rx.Observable
import server.RequestController
import server.RequestResult


fun handle(request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf>): Observable<Void> {
    val requestUri = request.decodedPath
    val result: RequestResult = if (requestUri.isNullOrEmpty()) {
        RequestResult(HttpResponseStatus.BAD_REQUEST, Observable.just("Invalid request."))
    } else {
        RequestController.handle(requestUri.substring(1), request.queryParameters)
    }
    response.status = result.status
    return response.writeString(result.message)
}

fun main(args: Array<String>) {
    BasicConfigurator.configure()
    HttpServer
        .newServer(8080)
        .start { req, resp ->
            handle(req, resp)
        }
        .awaitShutdown()
}