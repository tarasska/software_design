import db.AccountImpl;
import io.reactivex.netty.protocol.http.server.HttpServer;
import org.apache.log4j.BasicConfigurator;
import serever.AbstractController;
import server.AccountController;


public class Launcher {
    public static int DEFAULT_PORT = 8081;

    public static void main(String[] args) {
        BasicConfigurator.configure();

        AccountController controller = new AccountController(new AccountImpl());

        HttpServer.newServer(DEFAULT_PORT).start((request, response) ->
            AbstractController.handle(controller, request, response)
        ).awaitShutdown();
    }
}