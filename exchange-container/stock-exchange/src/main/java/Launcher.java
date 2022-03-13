import db.StockExchangeDao;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import org.apache.log4j.BasicConfigurator;
import rx.Observable;
import serever.StockExchangeController;

public class Launcher {
    public static int DEFAULT_PORT = 8080;


    private static Observable<Void> handle(
        StockExchangeController controller,
        HttpServerRequest<ByteBuf> request,
        HttpServerResponse<ByteBuf> response
    ) {
        String requestUri = request.getDecodedPath();
        if (requestUri == null || requestUri.isBlank()) {
            response.setStatus(HttpResponseStatus.BAD_REQUEST);
            return response.writeString(Observable.just("Invalid request."));
        } else {
            Observable<String> result = controller.handle(
                requestUri.substring(1),
                request.getQueryParameters()
            );
            response.setStatus(HttpResponseStatus.OK);
            return response.writeString(result);
        }
    }

    public static void main(String[] args) {

        StockExchangeController controller = new StockExchangeController(new StockExchangeDao());

        BasicConfigurator.configure();

        HttpServer.newServer(DEFAULT_PORT).start((request, response) ->
            handle(controller, request, response)
        ).awaitShutdown();
    }
}
