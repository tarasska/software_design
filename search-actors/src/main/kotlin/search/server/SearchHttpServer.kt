package search.server

import org.http4k.core.HttpHandler
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

interface SearchHttpServer {
    val server: Http4kServer
        get() = handler().asServer(Jetty(8080))

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop()
    }

    fun handler(): HttpHandler
}