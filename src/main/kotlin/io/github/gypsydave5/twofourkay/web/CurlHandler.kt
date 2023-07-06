package io.github.gypsydave5.twofourkay.web

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.parse.curl.parseCurl
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes

class CurlHandler : HttpHandler by routes(
    Method.POST bind { request ->
        request.bodyString()
            .parseCurl()
            .map { it.generateKotlin() }
            .map { Response(Status.OK).body(it) }
            .recover { Response(Status.BAD_REQUEST).body(it.message ?: "unknown error") }
    }
)
