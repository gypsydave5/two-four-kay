package io.github.gypsydave5.twofourkay.web

import dev.forkhandles.result4k.asResultOr
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.har.HAR
import io.github.gypsydave5.twofourkay.har.parseHar
import io.github.gypsydave5.twofourkay.har.toHttpTransactions
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.io.FileNotFoundException

class App : HttpHandler {
    override fun invoke(request: Request): Response {
        return routing(request)
    }
}

private fun routing(request: Request): Response {
    return routes(
        "/har" bind harHandler,
        "/" bind Method.GET to rootHandler
    )(request)
}

private val harHandler = routes(
    Method.POST bind { request ->
        request.bodyString().parseHar()
            .map(HAR::toHttpTransactions)
            .map(List<HttpTransaction>::generateKotlin)
            .map { Response(Status.OK).body(it) }
            .recover { Response(Status.BAD_REQUEST).body(it.toString()) }
    },

    Method.GET bind { request ->
        request.query("har")
            .asResultOr { IllegalArgumentException("Missing har parameter in query string") }
            .flatMap { it.parseHar() }
            .map(HAR::toHttpTransactions)
            .map(List<HttpTransaction>::generateKotlin)
            .map { Response(Status.OK).body(it) }
            .recover { Response(Status.BAD_REQUEST).body(it.message ?: "unknown error") }
    }
)


private val rootHandler = { request: Request ->
    Response(Status.OK).body(getResourceAsText("/index.html"))
}

private fun getResourceAsText(path: String): String =
    object {}.javaClass.getResource(path)?.readText() ?: throw FileNotFoundException(path)

