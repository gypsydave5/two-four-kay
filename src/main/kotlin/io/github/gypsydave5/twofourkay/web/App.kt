package io.github.gypsydave5.twofourkay.web

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.har.parseHar
import io.github.gypsydave5.twofourkay.har.toHttpTransactions
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status

class App : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.OK).body(request.bodyString().parseHar().toHttpTransactions().generateKotlin())
    }
}