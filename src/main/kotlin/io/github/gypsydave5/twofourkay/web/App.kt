package io.github.gypsydave5.twofourkay.web

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.har.parseHar
import io.github.gypsydave5.twofourkay.har.toHttpTransactions
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.io.FileNotFoundException

class App : HttpHandler {
    override fun invoke(request: Request): Response {
        return routing(request)
        // return Response(Status.OK).body(request.bodyString().parseHar().toHttpTransactions().generateKotlin())
    }
}

private fun routing(request: Request): Response {
    return routes(
        "/har" bind Method.POST to harHandler,
        "/" bind Method.GET to rootHandler
    )(request)
}

private val harHandler = { request: Request ->
    Response(Status.OK).body(request.bodyString().parseHar().toHttpTransactions().generateKotlin())
}

private val rootHandler = { request: Request ->
    Response(Status.OK).body(getResourceAsText("/index.html"))
}

private fun getResourceAsText(path: String): String =
    object {}.javaClass.getResource(path)?.readText() ?: throw FileNotFoundException(path)

