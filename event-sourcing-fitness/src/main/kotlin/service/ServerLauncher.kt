package service

import io.reactivex.netty.protocol.http.server.HttpServer
import org.apache.log4j.BasicConfigurator

class ServerLauncher(
    private val controller: AbstractController,
    private val port: Int
) {
    fun launch() {
        BasicConfigurator.configure()
        HttpServer
            .newServer(port)
            .start { req, resp ->
                controller.handle(req, resp)
            }
            .awaitShutdown()
    }
}