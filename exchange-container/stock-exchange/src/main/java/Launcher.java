import io.reactivex.netty.protocol.http.server.HttpServer;
import org.apache.log4j.BasicConfigurator;
import rx.Observable;

public class Launcher {
    public static int DEFAULT_PORT = 8080;

    public static void main(String[] args) {

        // server = Server(Dao())

        BasicConfigurator.configure();
        HttpServer.newServer(DEFAULT_PORT).start((request, response) -> {
            Observable<String> result = Observable.just("Stub, server response");
            return response.writeString(result);
        }).awaitShutdown();
    }
}
