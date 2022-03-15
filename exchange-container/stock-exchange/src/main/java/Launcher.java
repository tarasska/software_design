import db.StockExchangeDao;
import io.reactivex.netty.protocol.http.server.HttpServer;
import org.apache.log4j.BasicConfigurator;
import serever.AbstractController;
import serever.StockExchangeController;

public class Launcher {
    public static int DEFAULT_PORT = 8080;


    public static void main(String[] args) {
        BasicConfigurator.configure();

        StockExchangeController controller = new StockExchangeController(new StockExchangeDao());

        HttpServer.newServer(DEFAULT_PORT).start((request, response) ->
            AbstractController.handle(controller, request, response)
        ).awaitShutdown();
    }
}
