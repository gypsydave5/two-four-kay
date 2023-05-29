import io.github.gypsydave5.twofourkay.web.App
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    val app = App()
    app.asServer(Undertow(8080)).start()
}