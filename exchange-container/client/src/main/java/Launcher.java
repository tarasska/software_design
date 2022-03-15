import db.AccountDao;
import io.reactivex.netty.protocol.http.server.HttpServer;
import org.apache.log4j.BasicConfigurator;
import serever.AbstractController;
import server.AccountController;


public class Launcher {
    public static int DEFAULT_PORT = 8081;

    public static void main(String[] args) {

        AccountController controller = new AccountController(new AccountDao());

        BasicConfigurator.configure();

        HttpServer.newServer(DEFAULT_PORT).start((request, response) ->
            AbstractController.handle(controller, request, response)
        ).awaitShutdown();
    }
}
