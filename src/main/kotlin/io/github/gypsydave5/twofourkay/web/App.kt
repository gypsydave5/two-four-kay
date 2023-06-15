package io.github.gypsydave5.twofourkay.web

import org.http4k.core.*
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import java.io.FileNotFoundException

class App : HttpHandler {
    override fun invoke(request: Request): Response {
        return routing(request)
    }
}

private fun routing(request: Request): Response {
    return routes(
        "/har" bind harHandler,
        "/" bind Method.POST to parseHandler,
        "/" bind Method.GET to bob
    )(request)
}

private val bob = static(ResourceLoader.Directory("src/main/resources"))

private val rootHandler = { request: Request ->
    Response(Status.OK).body(getResourceAsText("/index.html"))
}

private fun getResourceAsText(path: String): String =
    object {}.javaClass.getResource(path)?.readText() ?: throw FileNotFoundException(path)

