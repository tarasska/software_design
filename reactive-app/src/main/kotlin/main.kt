import db.ReactiveDaoImpl
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import org.apache.log4j.BasicConfigurator
import rx.Observable
import server.RequestController
import server.RequestResult


fun handle(
    controller: RequestController,
    request: HttpServerRequest<ByteBuf>,
    response: HttpServerResponse<ByteBuf>
): Observable<Void> {
    val requestUri = request.decodedPath
    val result: RequestResult = if (requestUri.isNullOrEmpty()) {
        RequestResult(HttpResponseStatus.BAD_REQUEST, Observable.just("Invalid request."))
    } else {
        controller.handle(requestUri.substring(1), request.queryParameters)
    }
    response.status = result.status
    return response.writeString(result.message)
}

fun main(args: Array<String>) {
    val controller = RequestController(ReactiveDaoImpl())
    BasicConfigurator.configure()
    HttpServer
        .newServer(8080)
        .start { req, resp ->
            handle(controller, req, resp)
        }
        .awaitShutdown()
}