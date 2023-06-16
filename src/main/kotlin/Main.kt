import io.github.gypsydave5.twofourkay.web.App
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    val config = Configuration()

    val app = App(config)

    app.asServer(Undertow(config.server_port))
        .start()

    println("started on http://localhost:${config.server_port}")
}

